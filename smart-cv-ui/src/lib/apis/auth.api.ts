import {
  SignInBodySchema,
  SignInResponseSchema,
  RefreshBodySchema,
  TokensResponseSchema,
} from "../schemas/auth.schema";
import { restClient } from "../rest-client";

export async function signIn(
  data: SignInBodySchema,
  config?: AxiosRequestConfig<SignInBodySchema>

): Promise<SignInResponseSchema> {
  const response = await restClient.post<
    SignInResponseSchema,
    SignInBodySchema
  >("/user/command/sign-in",
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

export async function refresh(
  body: RefreshBodySchema,

): Promise<TokensResponseSchema> {
  const response = await restClient.post<TokensResponseSchema>(
    "/user/command/refresh",
    body,
    {
      headers: {
        "No-Auth": true,
      },
    },
  );
  return response.data;
}
