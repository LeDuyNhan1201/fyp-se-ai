import { gql, useQuery } from "@apollo/client";
import {
  searchCvsQuerySchema,
  SearchCvsQuerySchema,
  searchCvsResponseSchema
} from "../../lib/schemas/cv.schema";

export const SEARCH_CVS_QUERY = gql`
  query SearchCvs(
    $createdBy: String
    $jobId: String
    $cursor: String
    $limit: Int
  ) {
    findAll(
      createdBy: $createdBy
      jobId: $jobId
      cursor: $cursor
      limit: $limit
    ) {
      items {
        id
        jobId
        createdBy
        objectKey
        downloadUrl
        score
      }
      cursor
      hasNextPage 
    }
  }
`;

export const useSearchCvs = (filters: SearchCvsQuerySchema) => {
  const validatedFilters = searchCvsQuerySchema.parse(filters);

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
