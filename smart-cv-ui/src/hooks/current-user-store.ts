import { create } from "zustand";
import { 
  previewUserResponseSchema, 
  PreviewUserResponseSchema
} from "@/lib/schemas/auth.schema";
import Cookies from "js-cookie";

type CurrentUserStore = {
    currentUser: PreviewUserResponseSchema | null;
  actions: {
    setCurrentUser: (user: PreviewUserResponseSchema) => void;
    getCurrentUser: () => PreviewUserResponseSchema | undefined;
    clearCurrentUser: () => void;
  };
};

const CURRENT_USER_KEY = "current_user";

export const useCurrentUserStore = create<CurrentUserStore>(
  (set) => ({
    currentUser: null,
    actions: {
      setCurrentUser: (user) =>
        Cookies.set(CURRENT_USER_KEY, JSON.stringify(user), {
            path: "/",
            secure: true,
            sameSite: "Strict",
        }),
      getCurrentUser: () => {
        const storedUser = Cookies.get(CURRENT_USER_KEY);

        if (storedUser) {
            try {
                const parsedUser = JSON.parse(storedUser);
                const validatedUser = previewUserResponseSchema.parse(parsedUser);

                set({ currentUser: validatedUser });
                return validatedUser;

            } catch (error) {
                console.error("Failed to validate User:", error);
                localStorage.removeItem(CURRENT_USER_KEY);
            }
        }
        return undefined;  
      },

      clearCurrentUser: () => {
        Cookies.remove(CURRENT_USER_KEY, { path: "/" });
        set({ currentUser: null });
      },
    },
  }),
);

export const useCurrentUser = () =>
  useCurrentUserStore((state) => state.currentUser);
export const useCurrentUserActions = () =>
  useCurrentUserStore((state) => state.actions);
