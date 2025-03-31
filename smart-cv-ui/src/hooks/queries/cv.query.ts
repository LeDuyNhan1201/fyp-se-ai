import { gql, useQuery } from "@apollo/client";
import {
  searchCvsSchema,
  SearchCvsSchema,
  searchCvsResponseSchema
} from "../../lib/schemas/cv.schema";

export const SEARCH_CVS_QUERY = gql`
  query SearchCvs(
    $jobId: String!
    $education: [String!]
    $skills: [String!]
    $experience: [String!]
    $fromScore: Float
    $toScore: Float
    $cursor: String
    $limit: Int
  ) {
    searchJobs(
      jobId: $jobId
      education: $education
      skills: $skills
      experience: $experience
      fromScore: $fromScore
      toScore: $toScore
      cursor: $cursor
      limit: $limit
    ) {
      items {
        id
        jobId
        email
        phone
        education
        skills
        experience
        toSalary
        createdAt
      }
      cursor
      limit
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
