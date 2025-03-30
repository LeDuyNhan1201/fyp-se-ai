import { z } from "zod";
import { unauthorizedErrorResponseSchema } from "./errors.schema";

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

export const searchJobQuerySchema = z.object({
  organizationName: z.string().optional().nullable().default(null),
  position: z.string().optional().nullable().default(null),
  education: z.array(z.string()).optional().nullable().default([]),
  skills: z.array(z.string()).optional().nullable().default([]),
  experience: z.array(z.string()).optional().nullable().default([]),
  fromSalary: z.number().optional().nullable().default(null),
  toSalary: z.number().optional().nullable().default(null),
  page: z.number().int().positive().optional().nullable().default(1),
  size: z.number().int().positive().optional().nullable().default(3),
});
export type SearchJobQuerySchema = z.infer<typeof searchJobQuerySchema>;

export const searchJobsByUserParamsSchema = z.object({
  userId: z.string().uuid(),
});
export type SearchJobsByUserParamsSchema = z.infer<typeof searchJobsByUserParamsSchema>;

export const getJobsResponseSchema = z.object({
  items: z.array(jobDescriptionSchema).default([]),
  page: z.number().int().positive().default(1),
  size: z.number().int().positive().default(10),
  totalPages: z.number().positive().default(1),

});
export type GetJobsResponseSchema = z.infer<typeof getJobsResponseSchema>;

export const getTimelinePostsErrorResponseSchema = z.discriminatedUnion(
  "type",
  [unauthorizedErrorResponseSchema],
);
export type GetTimelinePostsErrorResponseSchema = z.infer<typeof getTimelinePostsErrorResponseSchema>;


