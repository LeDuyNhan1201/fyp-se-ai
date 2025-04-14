import { z } from "zod";

export const validationErrorResponseSchema = z.object({
  errorCode: z.literal("common/validation-error"),
  message: z.string(),
  errors: z.record(z.union([z.string(), z.array(z.string())])),
});
export type ValidationErrorResponseSchema = z.infer<typeof validationErrorResponseSchema>;

export const resourceNotFoundErrorResponseSchema = z.object({
  errorCode: z.literal("common/resource-not-found"),
  message: z.string(),
});
export type ResourceNotFoundErrorResponseSchema = z.infer<typeof resourceNotFoundErrorResponseSchema>;

export const unauthorizedErrorResponseSchema = z.object({
  errorCode: z.literal("common/unauthorized"),
  message: z.string(),
});
export type UnauthorizedErrorResponseSchema = z.infer<typeof unauthorizedErrorResponseSchema>;

export const forbiddenErrorResponseSchema = z.object({
  errorCode: z.literal("common/forbidden"),
  message: z.string(),
});
export type ForbiddenErrorResponseSchema = z.infer<typeof forbiddenErrorResponseSchema>;

