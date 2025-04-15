import {
  GetJobDetailsParamsSchema,
  JobDescriptionSchema,
  CreateJobBodySchema,
  CreateJobResponseSchema
} from "../schemas/job.schema"
import { restClient } from "../rest-client";

export async function getJobDetailsApi(
  params: GetJobDetailsParamsSchema,
): Promise<JobDescriptionSchema> {
  const response = await restClient.get<JobDescriptionSchema>(
    `/job/query/${params.id}`
  );
  return response.data;
}

export async function createJob(
    body: CreateJobBodySchema

): Promise<CreateJobResponseSchema> {
  const response = await restClient.post<CreateJobResponseSchema>(
    "/job/command",
    body,
  );
  return response.data;
}
