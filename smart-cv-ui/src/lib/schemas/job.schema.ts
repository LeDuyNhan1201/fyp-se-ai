import { z } from "zod";
import {
  unauthorizedErrorResponseSchema,
  validationErrorResponseSchema,
  forbiddenErrorResponseSchema,
  resourceNotFoundErrorResponseSchema,
} from "./errors.schema";

export const jobDescriptionSchema = z.object({
  id: z.string().uuid(),
  organizationName: z.string().optional(),
  email: z.string().email().nullable().optional(),
  phone: z.string().nullable().optional(),
  position: z.string().optional(),
  education: z.array(z.string()).optional(),
  skills: z.array(z.string()).optional(),
  experience: z.array(z.string()).optional(),
  fromSalary: z.number().optional(),
  toSalary: z.number().optional(),
  createdAt: z.string().datetime().optional(),
  expiredAt: z.string().datetime().optional(),
});
export type JobDescriptionSchema = z.infer<typeof jobDescriptionSchema>;

export const searchJobsSchema = z.object({
  organizationName: z.string().optional().nullable().default(null),
  position: z.string().optional().nullable().default(null),
  education: z.array(z.string()).optional().default([]),
  skills: z.array(z.string()).optional().default([]),
  experience: z.array(z.string()).optional().default([]),
  fromSalary: z.number().optional().nullable().default(null),
  toSalary: z.number().optional().nullable().default(null),
  page: z.number().int().positive().optional().nullable().default(1),
  size: z.number().int().positive().optional().nullable().default(3),
});
export type SearchJobsSchema = z.infer<typeof searchJobsSchema>;

export const searchJobsByUserParamsSchema = z.object({
  userId: z.string().uuid(),
});
export type SearchJobsByUserParamsSchema = z.infer<typeof searchJobsByUserParamsSchema>;

export const searchJobsResponseSchema = z.object({
  items: z.array(jobDescriptionSchema).default([]),
  page: z.number().int().positive().default(1),
  size: z.number().int().positive().default(3),
  totalPages: z.number().positive().default(1),

});
export type SearchJobsResponseSchema = z.infer<typeof searchJobsResponseSchema>;

export const createJobSchema = z.object({
  organizationName: z.string(),
  position: z.string(),
  fromSalary: z.number(),
  toSalary: z.number(),
  expiredAt: z.string().datetime(),
  details: z.string().max(300),
});
export type CreateJobSchema = z.infer<typeof createJobSchema>;

export const createJobResponseSchema = z.object({
  message: z.string(),
});
export type CreateJobResponseSchema = z.infer<typeof createJobResponseSchema>;

export const jobErrorResponseSchema = z.discriminatedUnion(
  "type",
  [
    unauthorizedErrorResponseSchema,
    validationErrorResponseSchema,
    forbiddenErrorResponseSchema,
    resourceNotFoundErrorResponseSchema,
  ],
);
export type JobErrorResponseSchema = z.infer<typeof jobErrorResponseSchema>;


