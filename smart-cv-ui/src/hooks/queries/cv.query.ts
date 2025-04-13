import { gql, useQuery } from "@apollo/client";
import {
  searchCvsSchema,
  SearchCvsSchema,
  searchCvsResponseSchema
} from "../../lib/schemas/cv.schema";

export const SEARCH_CVS_QUERY = gql`
  query SearchCvs(
    $cursor: String
    $limit: Int
  ) {
    findAll(
      cursor: $cursor
      limit: $limit
    ) {
      items {
        id
        objectKey
        downloadUrl
        score
      }
      cursor
      hasNextPage 
    }
  }
`;

export const useSearchCvs = (filters: SearchCvsSchema) => {
  const validatedFilters = searchCvsSchema.parse(filters);

  const { loading, error, data, fetchMore } = useQuery(SEARCH_CVS_QUERY, {
    variables: {
      ...validatedFilters,
      cursor: null,
      limit: validatedFilters.limit
    },
    notifyOnNetworkStatusChange: true,
  });

  if (error) return { loading, error, data: null, loadMore: () => { } };

  if (!data || !data.searchCvs)
    return { loading, error: null, data: null, loadMore: () => { } };

  const validatedData = searchCvsResponseSchema.parse(data.searchCvs);

  const cursor = validatedData.cursor;
  const hasNextPage = validatedData.hasNextPage;

  const loadMore = () => {
    if (!hasNextPage) return;
    fetchMore({
      variables: { ...validatedFilters, cursor },
      updateQuery: (prevResult, { fetchMoreResult }) => {
        if (!fetchMoreResult) return prevResult;
        return {
          searchCvs: {
            ...fetchMoreResult.searchCvs,
            items: [
              ...prevResult.searchCvs.items,
              ...fetchMoreResult.searchCvs.items
            ],
          },
        };
      },
    });
  };

  return { loading, error, data: validatedData, loadMore, cursor, hasNextPage };
};
