import { RestClientProvider } from "@/app/rest-client-provider";

export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen">
      <RestClientProvider>
        {children}
      </RestClientProvider>
    </div>
  );
}
