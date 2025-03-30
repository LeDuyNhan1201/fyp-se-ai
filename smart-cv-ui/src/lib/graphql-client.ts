import {
  ApolloClient,
  InMemoryCache,
  createHttpLink,
} from "@apollo/client";
import { relayStylePagination } from "@apollo/client/utilities";
import { setContext } from "@apollo/client/link/context";
import { getAccessToken } from "./utils";


export const createApolloClient = (
  serviceName: string,
  cacheType: InMemoryCache,
) => {
  const authLink = setContext((_, { headers }) => {
    return getAccessToken().then((token) => ({
      headers: {
        ...headers,
        Authorization: token ? `Bearer ${token}` : "",
      },
    }));
  });

  const httpLink = createHttpLink({
    uri: `${process.env.NEXT_PUBLIC_API_URL}/${serviceName}/graphql`,
    credentials: "include",
  });

  return new ApolloClient({
    link: authLink.concat(httpLink),
    cache: cacheType,
  });
};

export const searchCVsCache = new InMemoryCache({
  typePolicies: {
    Query: {
      fields: {
        searchCVs: relayStylePagination(),
      },
    },
  },
});
