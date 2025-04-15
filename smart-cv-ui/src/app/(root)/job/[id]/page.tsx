import { GraphqlClientProvider } from "@/app/graphql-client-provider";
import CvsList from "@/components/cvs-list";
import { createApolloClient, searchCVsCache } from "@/lib/graphql-client";
import { ApolloProvider } from "@apollo/client";

type JobDetailsProps = {
  params: { id: string };
};

export default async function JobDetailsPage({
  params
}: JobDetailsProps) {
  const { id } = params;
  return (
    <GraphqlClientProvider serviceName="cv">
        <section className="container mx-auto py-6">
          <h1 className="text-2xl font-bold mb-4">Job Details</h1>
          <CvsList jobId={id} />
        </section>
    </GraphqlClientProvider>
  );
};

