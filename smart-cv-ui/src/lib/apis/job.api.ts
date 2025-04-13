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
    `/job/${params.userId}/user`,
    query,
  );
  return response.data;
}

export async function createJob(
    body: CreateJobSchema

): Promise<CreateJobResponseSchema> {
  const response = await restClient.post<CreateJobResponseSchema>(
    "/job/command",
    body,
  );
  return response.data;
}
