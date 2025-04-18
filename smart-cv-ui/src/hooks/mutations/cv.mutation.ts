import { 
  ApplyCvRequestParamsSchema,
  ApplyCvResponseSchema,
  CvErrorResponseSchema,
  ApproveCvParamsSchema,
  ApproveCvBodySchema,
  ApproveCvResponseSchema,
} from "@/lib/schemas/cv.schema";
import { UploadFileBodySchema } from "@/lib/schemas/file.schema";
import { applyCvApi, approveCvApi } from "@/lib/apis/cv.api";
import { useMutation } from "@tanstack/react-query";
import { isAxiosError } from "axios";

export function useApplyCvMutation(
  params: ApplyCvRequestParamsSchema,
) {
  const mutationKey = ["cv", "create"] as const;
  return useMutation<
      ApplyCvResponseSchema,
      CvErrorResponseSchema,
      UploadFileBodySchema
    >({
    mutationKey,
    mutationFn: (body) => applyCvApi<ApplyCvResponseSchema>(params, body),
    throwOnError: (error) => isAxiosError(error),
  });
}

export function useApproveCvMutation(
  params: ApproveCvParamsSchema,
) {
  const mutationKey = ["cv", "approve-cv"] as const;
  return useMutation<
    ApproveCvResponseSchema,
    CvErrorResponseSchema,            
    ApproveCvBodySchema
  >({
    mutationKey,
    mutationFn: (body) => approveCvApi(params, body),
    throwOnError: (error) => isAxiosError(error),
  });
}
