import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  RefreshBodySchema,
  SignInBodySchema,
  SignInResponseSchema,
  SignInErrorResponseSchema,
  RefreshErrorResponseSchema,
  TokensResponseSchema
} from "@/lib/schemas/auth.schema"
import { 
  signIn,
  refresh
} from "@/lib/apis/auth.api";
import { isAxiosError } from "axios";
import { useTokenActions } from "../tokens-store";
import { useCurrentUserActions } from "../current-user-store";

export function useSignInMutation() {
  const client = useQueryClient();
  const { setAccessToken, setRefreshToken } = useTokenActions();
  const { setCurrentUser } = useCurrentUserActions();

  return useMutation<
    SignInResponseSchema,
    SignInErrorResponseSchema,
    SignInBodychema
  >({
    mutationKey: ["auth", "sign-in"],
    mutationFn: (body) => signIn(body),
    onSuccess(data) {
          setAccessToken(data.tokens.accessToken);
          setRefreshToken(data.tokens.refreshToken);
          setCurrentUser(data.user);
          client.invalidateQueries({
            queryKey: ["current-user"],
          });
    },
    throwOnError: (error) => isAxiosError(error),
  });
}

export function useRefreshMutation() {
  const { setAccessToken, setRefreshToken } = useTokenActions();

  return useMutation<
    TokensResponseSchema,
    RefreshErrorResponseSchema,
    RefreshBodySchema
  >({
    mutationKey: ["auth", "refresh"],
    mutationFn: (body) => refresh(body),
    onSuccess(data) {
          setAccessToken(data.tokens.accessToken);
          setRefreshToken(data.tokens.refreshToken);
    },
    throwOnError: (error) => isAxiosError(error),
  });
}
