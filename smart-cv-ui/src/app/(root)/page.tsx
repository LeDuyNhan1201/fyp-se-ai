"use client";

import JobList from "@/components/Jobs-list";
import {
  createApolloClient
} from "../../lib/graphql-client"
import { ApolloProvider, InMemoryCache } from "@apollo/client";

export default function Home() {
  const client = createApolloClient("job", new InMemoryCache());

  return (
    <ApolloProvider client={client}>
      <>
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
      </>
    </ApolloProvider>
  );
}
