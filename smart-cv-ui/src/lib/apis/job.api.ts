import {
  SearchJobsSchema,
  SearchJobsByUserParamsSchema,
  SearchJobsResponseSchema,
  CreateJobSchema,
  CreateJobResponseSchema
} from "../schemas/job.schema"
import { restClient } from "../rest-client";

export async function searchJobsApi(
  query?: SearchJobsSchema,
): Promise<SearchJobsResponseSchema> {
  const response = await restClient.get<SearchJobsResponseSchema>(
    "/job/graphql",
    query,
  );
  return response.data;
}

export async function searchJobsByUserApi(
  params: SearchJobsByUserParamsSchema,
  query?: SearchJobsSchema,
): Promise<SearchJobsResponseSchema> {
  const response = await restClient.get<SearchJobsResponseSchema>(
    `/job/${params.userId}/securityUser`,
    query,
  );
  return response.data;
}

export async function createJob(body: CreateJobSchema) {
  const response = await restClient.post<CreateJobResponseSchema>(
    "/job",
    body,
  );
  return response.data;
}
