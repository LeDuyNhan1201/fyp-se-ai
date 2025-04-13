import { useMutation } from "@tanstack/react-query";
import {
  SignInRequestSchema,
  SignInResponseSchema,
  signInErrorResponseSchema
} from "../../lib/schemas/auth.schema"
import { signIn } from "@/lib/apis/auth.api";
import { isAxiosError } from "axios";
import { useTokenActions } from "../use-token-store";
import { useCurrentProfileActions } from "../use-current-profile-store";

export function useSignInMutation() {
  const { setAccessToken, setRefreshToken } = useTokenActions();
  const { setCurrentProfile } = useCurrentProfileActions();

  return useMutation<
    SignInResponseSchema,
    signInErrorResponseSchema,
    SignInRequestSchema
  >({
    mutationKey: ["auth", "sign-in"],
    mutationFn: (body) => signIn(body),
    onSuccess(data) {
          setAccessToken(data.tokens.accessToken);
          setRefreshToken(data.tokens.refreshToken);
          setCurrentProfile(data.user);
    },
    throwOnError: (error) => isAxiosError(error),
  });
}
