import { z } from "zod";
import { tokenSchema } from "./tokens.schema";
import { validationErrorResponseSchema } from "./error.schema";
import {baseProfileResponseSchema} from "@/lib/api/schemas/profile.schema";

export const userSchema = z.object({
  id: z.coerce.string().uuid(),
  email: z.coerce.string(),
  name: z.coerce.string(),
});
export type UserSchema = z.infer<typeof userSchema>;

export const signInRequestSchema = z.object({
  email: z.coerce.string().email().min(1),
  password: z.coerce.string().min(6).max(20),
});
export type SignInRequestSchema = z.infer<typeof signInRequestSchema>;

export const signInResponseSchema = z.object({
  tokens: z.lazy(() => tokenSchema),
  user: z.lazy(() => userSchema),
});
export type SignInResponseSchema = z.infer<typeof signInResponseSchema>;

export const signInErrorResponseSchema = z.discriminatedUnion("type", [
  validationErrorResponseSchema,
]);
export type SignInErrorResponseSchema = z.infer<typeof signInErrorResponseSchema>;