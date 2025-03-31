import { uploadFileApi } from "@/lib/apis/file.api";
import { CreateCvResponseSchema } from "@/lib/schemas/cv.schema";
import { UploadFileSchema } from "@/lib/schemas/file.schema";
import { useMutation } from "@tanstack/react-query";

export function useUploadFileMutation() {
  const mutationKey = ["cv", "create"] as const;
  return useMutation({
    mutationKey,
    mutationFn: (body: UploadFileSchema) =>
      uploadFileApi<CreateCvResponseSchema>(body),
  });
}
