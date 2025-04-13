import {
  RefreshTokenBodySchema,
  RefreshTokenResponseSchema,
} from "../schemas/tokens.schema";
import { restClient } from "../rest-client";

export async function signIn(
  data: SignInRequestSchema,
  config?: AxiosRequestConfig<SignInRequestSchema>

): Promise<SignInResponseSchema> {
  const response = await apiClient.post<
    SignInResponseSchema,
    SignInRequestSchema
  >("/user/query/sign-in",
    data,
    {
        headers: {
          "No-Auth": true,
        },
        ...config,
    }
  );
  return response.data;
}

export async function refreshToken(
  body: RefreshTokenBodySchema,

): Promise<RefreshTokenResponseSchema> {
  const response = await restClient.post<RefreshTokenResponseSchema>(
    "/user/query/refresh",
    body,
    {
      headers: {
        "No-Auth": true,
      },
    },
  );
  return response.data;
}
