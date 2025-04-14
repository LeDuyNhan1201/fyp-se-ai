import { z } from "zod";

export const jwtPayloadSchema = z.object({
  sub: z.string(),
  exp: z.number(),
  iat: z.number(),
});
export type JwtPayloadSchema = z.infer<typeof jwtPayloadSchema>;

export const accessTokenPayloadSchema = jwtPayloadSchema;
export type AccessTokenPayloadSchema = z.infer<typeof accessTokenPayloadSchema>;

export const accessTokenSchema = z.string();
export type AccessTokenSchema = z.infer<typeof accessTokenSchema>;

export const refreshTokenPayloadSchema = jwtPayloadSchema;
export type RefreshTokenPayloadSchema = z.infer<typeof refreshTokenPayloadSchema>;

export const refreshTokenSchema = z.string();
export type RefreshTokenSchema = z.infer<typeof refreshTokenSchema>;

export const refreshTokenBodySchema = z.object({
  refreshToken: refreshTokenSchema,
});
export type RefreshTokenBodySchema = z.infer<typeof refreshTokenBodySchema>;

export const tokensResponseSchema = z.object({
  accessToken: accessTokenSchema,
  refreshToken: refreshTokenSchema,
});
export type TokensResponseSchema = z.infer<typeof tokensResponseSchema>;
