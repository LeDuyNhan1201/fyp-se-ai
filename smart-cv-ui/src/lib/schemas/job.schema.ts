import { z } from "zod";
import {
  unauthorizedErrorResponseSchema,
  validationErrorResponseSchema,
  forbiddenErrorResponseSchema,
  resourceNotFoundErrorResponseSchema,
} from "./errors.schema";

export const jobDescriptionSchema = z.object({
  id: z.string().uuid(),
  createdBy: z.string().uuid(),
  organizationName: z.string(),
  email: z.string().email().nullable().optional(),
  phone: z.string().nullable().optional(),
  position: z.string(),
  educations: z.array(z.string()),
  skills: z.array(z.string()),
  experiences: z.array(z.string()).optional(),
  fromSalary: z.number(),
  toSalary: z.number(),
  createdAt: z.string().datetime(),
  expiredAt: z.string().datetime(),
});
export type JobDescriptionSchema = z.infer<typeof jobDescriptionSchema>;

export const searchJobsSchema = z.object({
  organizationName: z.string().optional().nullable().default(null),
  position: z.string().optional().nullable().default(null),
  educations: z.array(z.string()).optional().default([]),
  skills: z.array(z.string()).optional().default([]),
  experiences: z.array(z.string()).optional().default([]),
  fromSalary: z.number().positive().optional().nullable().default(null),
  toSalary: z.number().positive().optional().nullable().default(null),
  page: z.number().int().positive().min(1).optional().nullable().default(1),
  size: z.number().int().positive().max(10).optional().nullable().default(3),
});
export type SearchJobsSchema = z.infer<typeof searchJobsSchema>;

export const getJobDetailsParamsSchema = z.object({
  id: z.string().uuid(),
});
export type GetJobDetailsParamsSchema = z.infer<typeof getJobDetailsParamsSchema>;

export const searchJobsResponseSchema = z.object({
  items: z.array(jobDescriptionSchema).default([]),
  page: z.number().int().positive().default(1),
  totalPages: z.number().positive().default(1),

});
export type SearchJobsResponseSchema = z.infer<typeof searchJobsResponseSchema>;

export const createJobBodySchema = z.object({
  organizationName: z.string(),
  position: z.string(),
  fromSalary: z.number(),
  toSalary: z.number(),
  expiredAt: z.date(),
  requirements: z.string().max(500),
});
export type CreateJobBodySchema = z.infer<typeof createJobSchema>;

export const createJobResponseSchema = z.object({
  message: z.string(),
});
export type CreateJobResponseSchema = z.infer<typeof createJobResponseSchema>;

export const jobErrorResponseSchema = z.discriminatedUnion(
  "errorCode",
  [
    unauthorizedErrorResponseSchema,
    validationErrorResponseSchema,
    forbiddenErrorResponseSchema,
    resourceNotFoundErrorResponseSchema,
  ],
);
export type JobErrorResponseSchema = z.infer<typeof jobErrorResponseSchema>;


