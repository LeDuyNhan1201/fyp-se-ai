import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"
import { COOKIE_KEY_ACCESS_TOKEN, COOKIE_KEY_REFRESH_TOKEN } from "@/constants";
import { getCookie, setCookie } from "cookies-next";
import { jwtDecode } from "jwt-decode";
import { accessTokenPayloadSchema } from "./schemas/tokens.schema";
import { refreshTokenApi } from "./apis/authentication.api";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function formatDate(date: string) {
  return new Date(date).toLocaleDateString("en-US", {
    month: "long",
    day: "numeric",
    year: "numeric",
  });
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
    refreshPromise = refreshTokenApi({ refreshToken })
      .then((response) => {
        const { accessToken: newAccessToken } = response;
        setCookie(COOKIE_KEY_ACCESS_TOKEN, newAccessToken);
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


