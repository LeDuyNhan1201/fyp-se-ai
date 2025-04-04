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
  email: z.string().email().nullable().optional(),
  phone: z.string().nullable().optional(),
  education: z.array(z.string()).optional(),
  skills: z.array(z.string()).optional(),
  experience: z.array(z.string()).optional(),
  score: z.number().optional().nullable().default(null),
  expiredAt: z.string().datetime().optional(),
});
export type CurriculumVitaeSchema = z.infer<typeof curriculumVitaeSchema>;

export const searchCvsSchema = z.object({
  jobId: z.string().uuid(),
  education: z.array(z.string()).optional().nullable().default([]),
  skills: z.array(z.string()).optional().nullable().default([]),
  experience: z.array(z.string()).optional().nullable().default([]),
  fromScore: z.number().optional().nullable().default(null),
  toScore: z.number().optional().nullable().default(null),
  cursor: z.string().optional().nullable().default(null),
  limit: z.number().int().positive().optional().nullable().default(3),
});
export type SearchCvsSchema = z.infer<typeof searchCvsSchema>;

export const searchCvsByUserParamsSchema = z.object({
  userId: z.string().uuid(),
});
export type SearchCvsByUserParamsSchema = z.infer<typeof searchCvsByUserParamsSchema>;

export const searchCvsResponseSchema = z.object({
  items: z.array(curriculumVitaeSchema).default([]),
  cursor: z.string().optional().nullable().default(null),
  limit: z.number().int().positive().default(3),
  hasNextPage: z.boolean().default(false),

});
export type SearchCvsResponseSchema = z.infer<typeof searchCvsResponseSchema>;

export const createCvResponseSchema = z.object({
  message: z.string(),
});
export type CreateCvResponseSchema = z.infer<typeof createCvResponseSchema>;

export const cvErrorResponseSchema = z.discriminatedUnion(
  "type",
  [
    unauthorizedErrorResponseSchema,
    validationErrorResponseSchema,
    forbiddenErrorResponseSchema,
    resourceNotFoundErrorResponseSchema,
  ],
);
export type CvErrorResponseSchema = z.infer<typeof cvErrorResponseSchema>;
