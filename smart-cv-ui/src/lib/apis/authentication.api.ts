import {
  RefreshTokenBodySchema,
  RefreshTokenResponseSchema,
} from "../schemas/tokens.schema";
import { restClient } from "../rest-client";

export async function refreshTokenApi(
  body: RefreshTokenBodySchema,

): Promise<RefreshTokenResponseSchema> {
  const response = await restClient.post<RefreshTokenResponseSchema>(
    "/auth/refresh",
    body,
    {
      headers: {
        "No-Auth": true,
      },
    },
  );
  return response.data;
}
