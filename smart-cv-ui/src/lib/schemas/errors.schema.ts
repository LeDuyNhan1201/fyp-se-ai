import { z } from "zod";

export const validationErrorResponseSchema = z.object({
  type: z.literal("ValidationError"),
  message: z.string(),
  errors: z.record(z.union([z.string(), z.array(z.string())])),
});
export type ValidationErrorResponseSchema = z.infer<typeof validationErrorResponseSchema>;

export const resourceNotFoundErrorResponseSchema = z.object({
  type: z.literal("ResourceNotFound"),
  message: z.string(),
});
export type ResourceNotFoundErrorResponseSchema = z.infer<typeof resourceNotFoundErrorResponseSchema>;

export const unauthorizedErrorResponseSchema = z.object({
  type: z.literal("Unauthorized"),
  message: z.string(),
});
export type UnauthorizedErrorResponseSchema = z.infer<typeof unauthorizedErrorResponseSchema>;

export const forbiddenErrorResponseSchema = z.object({
  type: z.literal("Forbidden"),
  message: z.string(),
});
export type ForbiddenErrorResponseSchema = z.infer<typeof forbiddenErrorResponseSchema>;

