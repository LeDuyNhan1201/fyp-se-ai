import {
  GetJobsResponseSchema,
  SearchJobQuerySchema,
  SearchJobsByUserParamsSchema
} from "../schemas/job.schema"
import { restClient } from "../rest-client";

export async function searchJobsApi(
  query?: SearchJobQuerySchema,
): Promise<GetJobsResponseSchema> {
  const response = await restClient.get<GetJobsResponseSchema>(
    "/jobs/graphql",
    query,
  );
  return response.data;
}

export async function searchJobsByUserApi(
  params: SearchJobsByUserParamsSchema,
  query?: SearchJobQuerySchema,
): Promise<GetJobsResponseSchema> {
  const response = await restClient.get<GetJobsResponseSchema>(
    `/jobs/${params.userId}/user`,
    query,
  );
  return response.data;
}
