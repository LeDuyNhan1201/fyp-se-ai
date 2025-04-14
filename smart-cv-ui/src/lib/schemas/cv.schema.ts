import { z } from "zod";
import {
  unauthorizedErrorResponseSchema,
  validationErrorResponseSchema,
  forbiddenErrorResponseSchema,
  resourceNotFoundErrorResponseSchema,
} from "./errors.schema";

export const curriculumVitaeSchema = z.object({
  id: z.string().uuid(),
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

export const cvTagSchema = z.object({
  id: z.string().uuid(),
  jobId: z.string().uuid(),
  createdBy: z.string(),
  objectKey: z.string(),
  downloadUrl: z.string().url(),
  score: z.number(),
});
export type CvTagSchema = z.infer<typeof cvTagSchema>;


export const searchCvsQuerySchema = z.object({
//   jobId: z.string().uuid(),
//   education: z.array(z.string()).optional().nullable().default([]),
//   skills: z.array(z.string()).optional().nullable().default([]),
//   experience: z.array(z.string()).optional().nullable().default([]),
//   fromScore: z.number().optional().nullable().default(null),
//   toScore: z.number().optional().nullable().default(null),
  jobId: z.string().uuid().optional().nullable().default(null),
  createdBy: z.string().uuid().optional().nullable().default(null),
  cursor: z.string().optional().nullable().default(null),
  limit: z.number().int().positive().optional().nullable().default(3),
});
export type SearchCvsQuerySchema = z.infer<typeof searchCvsQuerySchema>;

export const searchCvsResponseSchema = z.object({
  items: z.array(cvTagSchema).default([]),
  cursor: z.string().optional().nullable().default(null),
  hasNextPage: z.boolean().default(false),
});
export type SearchCvsResponseSchema = z.infer<typeof searchCvsResponseSchema>;

export const createCvResponseSchema = z.object({
  message: z.string(),
});
export type CreateCvResponseSchema = z.infer<typeof createCvResponseSchema>;

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
