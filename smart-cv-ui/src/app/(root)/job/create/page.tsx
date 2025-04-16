import CreateJobForm from "@/components/create-job-form";
import { RestClientProvider } from "@/app/rest-client-provider";

const Page = async () => {
  return (
    <RestClientProvider>
      <section className="pink_container !min-h-[230px]">
        <h1 className="heading">Submit Your Job Description</h1>
      </section>

      <section className="flex justify-center items-center py-10">
        <CreateJobForm />
      </section>    
    </RestClientProvider>
  );
};

export default Page;
