import { gql, useQuery } from "@apollo/client";
import {
  searchJobsSchema,
  SearchJobsSchema,
  searchJobsResponseSchema
} from "../../lib/schemas/job.schema";

export const SEARCH_JOBS_QUERY = gql`
  query SearchJobs(
    $organizationName: String
    $position: String
    $educations: [String!]
    $skills: [String!]
    $experiences: [String!]
    $fromSalary: Float
    $toSalary: Float
    $page: Int
    $size: Int
  ) {
    search(
      organizationName: $organizationName
      position: $position
      educations: $educations
      skills: $skills
      experiences: $experiences
      fromSalary: $fromSalary
      toSalary: $toSalary
      page: $page
      size: $size
    ) {
      items {
        id
        createdBy
        organizationName
        email
        phone
        position
        educations
        skills
        experiences
        fromSalary
        toSalary
        createdAt
        expiredAt
      }
      page
      totalPages
    }
  }
`;

export const useSearchJobs = (filters: SearchJobsSchema) => {
  const validatedFilters = searchJobsSchema.parse(filters);
  const { loading, error, data, refetch } = useQuery(SEARCH_JOBS_QUERY, {
    variables: validatedFilters,
    notifyOnNetworkStatusChange: true,
  });
  console.log(validatedFilters);
  if (error) {
    console.log(error);
    return { loading, error, data: null, goToPage: () => { } };
  }
  console.log("Jobs data:", data);
  if (!data || !data.search)
    return { loading, error: null, data: null, goToPage: () => { } };

  const validatedData = searchJobsResponseSchema.parse(data.search);

  const goToPage = (page: number) => {
    if (page < 1 || page > validatedData.totalPages) return;
    refetch({ ...validatedFilters, page });
  };

  return { loading, error, data: validatedData, goToPage };
};

