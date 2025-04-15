import {
  ApolloClient,
  InMemoryCache,
  createHttpLink,
} from "@apollo/client";
import { relayStylePagination } from "@apollo/client/utilities";
import { setContext } from "@apollo/client/link/context";
import { getAccessToken } from "./utils";

const cvCache = new InMemoryCache({
  typePolicies: {
    Query: {
      fields: {
        searchCVs: relayStylePagination(),
      },
    },
  },
});

export const createApolloClient = (
  serviceName: string,
): ApolloClient<any> => {
  let cache: InMemoryCache;

  switch (serviceName) {
    case "cv":
      cache = cvCache;
      break;
    default:
      cache = new InMemoryCache();       
    break;
  }

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
    cache,
  });
};
