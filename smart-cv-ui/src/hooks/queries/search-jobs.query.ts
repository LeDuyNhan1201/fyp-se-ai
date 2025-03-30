import { gql, useQuery } from "@apollo/client";
import {
  searchJobQuerySchema,
  SearchJobQuerySchema,
  getJobsResponseSchema
} from "../../lib/schemas/job.schema";

export const SEARCH_JOBS_QUERY = gql`
  query SearchJobs(
    $organizationName: String
    $position: String
    $education: [String!]
    $skills: [String!]
    $experience: [String!]
    $fromSalary: Float
    $toSalary: Float
    $page: Int
    $size: Int
  ) {
    searchJobs(
      organizationName: $organizationName
      position: $position
      education: $education
      skills: $skills
      experience: $experience
      fromSalary: $fromSalary
      toSalary: $toSalary
      page: $page
      size: $size
    ) {
      items {
        id
        organizationName
        email
        phone
        position
        education
        skills
        experience
        fromSalary
        toSalary
        createdAt
        expiredAt
      }
      page
      size
      totalPages
    }
  }
`;

export const useSearchJobs = (filters: SearchJobQuerySchema) => {
  const validatedFilters = searchJobQuerySchema.parse(filters);
  const { loading, error, data, refetch } = useQuery(SEARCH_JOBS_QUERY, {
    variables: validatedFilters,
    notifyOnNetworkStatusChange: true,
  });
  console.log(validatedFilters);
  if (error) return { loading, error, data: null, goToPage: () => { } };

  if (!data || !data.searchJobs)
    return { loading, error: null, data: null, goToPage: () => { } };

  const validatedData = getJobsResponseSchema.parse(data.searchJobs);

  const goToPage = (page: number) => {
    if (page < 1 || page > validatedData.totalPages) return;
    refetch({ ...validatedFilters, page });
  };

  return { loading, error, data: validatedData, goToPage };
};

