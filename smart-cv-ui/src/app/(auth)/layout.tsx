import { RestClientProvider } from "@/app/rest-client-provider";

export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <main className="font-work-sans">
      <RestClientProvider>
        {children}
      </RestClientProvider>
    </main>
  );
}
