import { z } from "zod";

const UPLOAD_PHOTO_MAX_SIZE_MB = 10;

export const uploadFileSchema = z.object({
  photo: z
    .instanceof(File)
    .refine((file) => file.size < UPLOAD_PHOTO_MAX_SIZE_MB * 1024 * 1024, {
      message: `Photo size should be less than ${UPLOAD_PHOTO_MAX_SIZE_MB} MB`,
    }),
});
export type UploadFileSchema = z.infer<typeof uploadFileSchema>;


