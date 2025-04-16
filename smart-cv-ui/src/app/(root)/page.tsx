import { GraphqlClientProvider } from "@/app/graphql-client-provider";
import JobList from "@/components/Jobs-list";
import { InMemoryCache } from "@apollo/client";
import { Toaster } from "@/components/ui/sonner";

export default function Home() {
  return (
    <GraphqlClientProvider serviceName="job">
      <section className="pink_container">
        <h1 className="heading">
          Apply your resumes, <br />
          Connect With Enterprises!
        </h1>

        <p className="sub-heading !max-w-3xl">
          Submit CV, Explore jobs, and Automatically score your CVs.
        </p>
      </section>

      <section className="section_container">
        <p className="text-30-semibold">
        </p>
        <JobList />
      </section>
    </GraphqlClientProvider>
  );
}

