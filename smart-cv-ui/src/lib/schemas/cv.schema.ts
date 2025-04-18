import { z } from "zod";
import {
  unauthorizedErrorResponseSchema,
  validationErrorResponseSchema,
  forbiddenErrorResponseSchema,
  resourceNotFoundErrorResponseSchema,
} from "./errors.schema";

export const curriculumVitaeSchema = z.object({
  id: z.string(),
  jobId: z.string().uuid(),
  createdBy: z.string().uuid(),
  email: z.string().email().nullable().optional(),
  phone: z.string().nullable().optional(),
  educations: z.array(z.string()).optional(),
  skills: z.array(z.string()).optional(),
  experiences: z.array(z.string()).optional(),
  score: z.number().optional().nullable().default(null),
});
export type CurriculumVitaeSchema = z.infer<typeof curriculumVitaeSchema>;

export const curriculumVitaeStatusSchema = z.enum([
  "Approved",
  "Rejected",
  "Pending",
]);
export type CurriculumVitaeStatusSchema = z.infer<typeof curriculumVitaeStatusSchema>;

export const cvTagSchema = z.object({
  id: z.string(),
  jobId: z.string().uuid(),
  createdBy: z.string(),
  objectKey: z.string(),
  downloadUrl: z.string().url(),
  score: z.number(),
  status: curriculumVitaeStatusSchema,
});
export type CvTagSchema = z.infer<typeof cvTagSchema>;

export const searchCvsQuerySchema = z.object({
  jobId: z.string().uuid().optional().nullable().default(null),
  createdBy: z.string().uuid().optional().nullable().default(null),
  cursor: z.string().optional().nullable().default(null),
  limit: z.number().int().positive().max(10).optional().nullable().default(5),
});
export type SearchCvsQuerySchema = z.infer<typeof searchCvsQuerySchema>;

export const searchCvsResponseSchema = z.object({
  items: z.array(cvTagSchema).default([]),
  cursor: z.string().optional().nullable().default(null),
  hasNextPage: z.boolean().default(false),
});
export type SearchCvsResponseSchema = z.infer<typeof searchCvsResponseSchema>;

export const applyCvRequestParamsSchema = z.object({
  jobId: z.string().uuid(),
});
export type ApplyCvRequestParamsSchema = z.infer<typeof applyCvRequestParamsSchema>;

export const applyCvResponseSchema = z.object({
  message: z.string(),
});
export type ApplyCvResponseSchema = z.infer<typeof applyCvResponseSchema>;

export const approveCvParamsSchema = z.object({
  jobId: z.string().uuid(),
});
export type ApproveCvParamsSchema = z.infer<typeof approveCvParamsSchema>;

export const approveCvBodySchema = z.object({
  title: z.string().max(200).optional().nullable().default(null),
  content: z.string().max(1000).optional().nullable().default(null),
  receiverId: z.string().uuid(),
  cvId: z.string().uuid(),
});
export type ApproveCvBodySchema = z.infer<typeof approveCvBodySchema>;

export const approveCvResponseSchema = z.object({
  message: z.string(),
});
export type ApproveCvResponseSchema = z.infer<typeof approveCvResponseSchema>;

export const cvErrorResponseSchema = z.discriminatedUnion(
  "errorCode",
  [
    unauthorizedErrorResponseSchema,
    validationErrorResponseSchema,
    forbiddenErrorResponseSchema,
    resourceNotFoundErrorResponseSchema,
  ],
);
export type CvErrorResponseSchema = z.infer<typeof cvErrorResponseSchema>;
