import { RestClientProvider } from "@/app/rest-client-provider";
import JobDetailsSection from "@/components/job-details-section";

type JobDetailsProps = {
  params: { id: string };
};

export default function JobDetailsPage({
  params
}: JobDetailsProps) {
  const { id } = params;
  return (
    <RestClientProvider serviceName="cv">
      <JobDetailsSection id={id} />
    </RestClientProvider>
  );
};

