import { restClient } from "../rest-client";
import {
  UploadFileBodySchema
} from "../schemas/file.schema"
import {
  ApplyCvRequestParamsSchema,
  ApplyCvResponseSchema
} from "../schemas/cv.schema"

export async function applyCvApi<ApplyCvResponseSchema>(
  params: ApplyCvRequestParamsSchema,
  body: UploadFileBodySchema
): Promise<ApplyCvResponseSchema> {
  const formData = new FormData();
  formData.append("file", body.file);

  const response = await restClient.post<TResponse>(
    `/file/command/${params.jobId}`,
    formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  }
  );

  return response.data;
