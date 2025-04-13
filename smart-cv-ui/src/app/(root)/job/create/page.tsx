import CreateJobForm from "@/components/create-job-form";

const Page = async () => {
  return (
    <>
      <section className="pink_container !min-h-[230px]">
        <h1 className="heading">Submit Your Job Description</h1>
      </section>

      <CreateJobForm />
    </>
  );
};

export default Page;