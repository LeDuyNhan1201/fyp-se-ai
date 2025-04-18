import { restClient } from "../rest-client";
import {
  UploadFileBodySchema
} from "../schemas/file.schema"
import {
  ApplyCvRequestParamsSchema,
  ApplyCvResponseSchema,
  ApproveCvParamsSchema,
  ApproveCvBodySchema,
  ApproveCvResponseSchema,
} from "../schemas/cv.schema"

export async function applyCvApi<ApplyCvResponseSchema>(
  params: ApplyCvRequestParamsSchema,
  body: UploadFileBodySchema
): Promise<ApplyCvResponseSchema> {
  const formData = new FormData();
  formData.append("file", body.file);

  const response = await restClient.post<
    ApplyCvResponseSchema
  >(
    `/file/command/${params.jobId}`,
    formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  }
  );

  return response.data;
}

export async function approveCvApi<TResponse>(
  params: ApproveCvParamsSchema,
  body: ApproveCvBodySchema,
): Promise<ApproveCvResponseSchema> {
  
  const response = await restClient.post<ApproveCvResponseSchema>(
    `/cv/command/approve/${params.jobId}/job`, 
    body
  );

  return response.data;
}

