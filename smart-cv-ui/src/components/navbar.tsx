"use client";

import Link from "next/link";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { useCurrentUserActions } from "@/hooks/current-user-store";
import { useTokenActions } from "@/hooks/tokens-store";
import { BadgePlus } from "lucide-react";

const Navbar = () => {
  const router = useRouter();
  const { getCurrentUser, clearCurrentUser } = useCurrentUserActions();
  const { clearTokens } = useTokenActions();
  const currentUser = getCurrentUser();

  const handleSignOut = () => {
    clearCurrentUser();
    clearTokens();
    router.push("/sign-in");
  };

  return (
    <header className="px-5 py-3 bg-white shadow-sm font-work-sans">
      <nav className="flex justify-between items-center">
        <Link href="/">
          <Image src="/icon.svg" alt="logo" width={50} height={30} />
        </Link>

        <div className="flex items-center gap-5 text-black">
          {currentUser && currentUser?.name ? (
            <>
              <Link href="/job/create">
                <span className="max-sm:hidden">New job</span>
                <BadgePlus className="size-6 sm:hidden" />
              </Link>

              <Link href={`/user/${currentUser?.id}`}>
                <div className="flex items-center gap-2">
                  <Avatar className="size-10">
                    <AvatarImage
                      src={currentUser?.image || "https://github.com/shadcn.png"}
                      alt={currentUser?.name || ""}
                    />
                    <AvatarFallback>AV</AvatarFallback>
                  </Avatar>
                  <p>{currentUser?.name}</p>
                </div>
              </Link>

              <button onClick={handleSignOut}>
                <Image src="/sign-out.png" alt="sign-out" width={30} height={30} />
              </button>

            </>
          ) : (
            <Link href="/sign-in">
              Sign In
            </Link>
          )}
        </div>
      </nav>
    </header>
  );
};

export default Navbar;

