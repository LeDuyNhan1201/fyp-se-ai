"use client";

import JobList from "@/components/Jobs-list";
import {
  createApolloClient
} from "../../lib/graphql-client"
import { ApolloProvider, InMemoryCache } from "@apollo/client";

export default function Home() {
  const client = createApolloClient("slaveJob", new InMemoryCache());

  return (
    <ApolloProvider client={client}>
      <>
        <section className="pink_container">
          <h1 className="heading">
            Pitch Your Startup, <br />
            Connect With Entrepreneurs
          </h1>

          <p className="sub-heading !max-w-3xl">
            Submit Ideas, Vote on Pitches, and Get Noticed in Virtual
            Competitions.
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
