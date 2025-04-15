import { z } from "zod";

const UPLOAD_FILE_MAX_SIZE_MB = 20;

export const uploadFileBodySchema = z.object({
  file: z
    .instanceof(File)
    .refine((mFile) => mFile.size < UPLOAD_FILE_MAX_SIZE_MB * 2 * 1024 * 1024, {
      message: `File size should be less than ${UPLOAD_FILE_MAX_SIZE_MB} MB`,
    }),
});
export type UploadFileBodySchema = z.infer<typeof uploadFileBodySchema>;


