import { 
  ApplyCvRequestParamsSchema,
  ApplyCvResponseSchema,
  CvErrorResponseSchema
} from "@/lib/schemas/cv.schema";
import { UploadFileBodySchema } from "@/lib/schemas/file.schema";
import { applyCvApi } from "@/lib/apis/cv.api";
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
