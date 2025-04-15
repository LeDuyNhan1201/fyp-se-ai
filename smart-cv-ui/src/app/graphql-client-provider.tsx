"use client";

import { useMemo } from "react";
import { ApolloProvider } from "@apollo/client";
import { createApolloClient } from "@/lib/graphql-client";

type GraphqlClientProviderProps = {
  serviceName: string;
  children: React.ReactNode;
};

export function GraphqlClientProvider({
  serviceName,
  cache,
  children,
}: GraphqlClientProviderProps) {
  const apolloClient = useMemo(
    () => createApolloClient(serviceName),
    [serviceName]
  );

  return (
    <ApolloProvider client={apolloClient}>
      {children}
    </ApolloProvider>
  );
}

