import { clsx, type ClassValue } from "clsx"
import { format } from "date-fns";
import { twMerge } from "tailwind-merge"
import { COOKIE_KEY_ACCESS_TOKEN, COOKIE_KEY_REFRESH_TOKEN } from "@/constants";
import { getCookie, setCookie } from "cookies-next";
import { jwtDecode } from "jwt-decode";
import { accessTokenPayloadSchema } from "./schemas/auth.schema";
import { refresh } from "./apis/auth.api";
import { FieldValues, Path, UseFormSetError } from "react-hook-form";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function isImage(url: string) {
  return /\.(jpg|jpeg|png|gif|webp)$/i.test(url);
}

export function isPdf(url: string) {
  return /\.pdf$/i.test(url);
}

export function formatDate(date: string) {
  return new Date(date).toLocaleDateString("en-US", {
    month: "long",
    day: "numeric",
    year: "numeric",
  });
}

export function formatDateFromInstant(instant: number) {
  return format(new Date(instant * 1000), "PPP"); 
}

export function mapFieldErrorToFormError<T extends FieldValues>(
  setError: UseFormSetError<T>,
  errors: Record<string, string[] | string>,
) {
  for (const [field, messages] of Object.entries(errors)) {
    if (Array.isArray(messages)) {
      return setError(field as unknown as Path<T>, {
        message: messages.join(", "),
      });
    }

    setError(field as unknown as Path<T>, {
      message: messages,
    });
  }
}

export async function getAccessToken(): Promise<string | null> {
  let isRefreshing = false;
  let refreshPromise: Promise<string> | null = null;
  let accessToken: string | undefined = undefined;

  try {
    accessToken = await getCookie(COOKIE_KEY_ACCESS_TOKEN);
    if (accessToken) {
      const payload = accessTokenPayloadSchema.parse(jwtDecode(accessToken));
      if (payload.exp * 1000 > Date.now()) {
        return accessToken;
      }
    }
  } catch (error) {
    console.error("Failed to parse access token payload", error);
  }

  let refreshToken: string | undefined = undefined;

  try {
    refreshToken = await getCookie(COOKIE_KEY_REFRESH_TOKEN);
    if (!refreshToken) {
      return null;
    }
  } catch (error) {
    console.error("Failed to get refresh token", error);
    return null;
  }

  if (!isRefreshing) {
    isRefreshing = true;
    refreshPromise = refresh({ refreshToken })
      .then((response) => {
        const { 
          accessToken: newAccessToken,
          refreshToken: newRefreshToken
        } = response;
        setCookie(COOKIE_KEY_ACCESS_TOKEN, newAccessToken);
        setCookie(COOKIE_KEY_REFRESH_TOKEN, newRefreshToken);
        return newAccessToken;
      })
      .catch((error) => {
        throw error;
      })
      .finally(() => {
        isRefreshing = false;
        refreshPromise = null;
      });
  }

  return refreshPromise;
}


