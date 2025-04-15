"use client";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { GraphqlClientProvider } from "@/app/graphql-client-provider";
import CvsList from "@/components/cvs-list";
import { createApolloClient, searchCVsCache } from "@/lib/graphql-client";
import { ApolloProvider } from "@apollo/client";
import { useCurrentUserActions } from "@/hooks/current-user-store";
import { useGetJobDetailsQuery } from "@/hooks/queries/job.query";
import { getJobDetailsParamsSchema } from "@/lib/schemas/job.schema";
import { uploadFileBodySchema } from "@/lib/schemas/file.schema";
import { useApplyCvMutation } from "@/hooks/mutations/cv.mutation";
import React, { useRef, useState } from "react";
import { toast } from "sonner";

type JobDetailsProps = {
  id: string;
};

export default function JobDetailsSection({ id }: JobDetailsProps) {
  const { getCurrentUser } = useCurrentUserActions();
  const currentUser = getCurrentUser();
  const { data: jobDetails } = useGetJobDetailsQuery(getJobDetailsParamsSchema.parse({ id }));
  const isOwner = currentUser?.id === jobDetails?.createdBy;

  const fileInputRef = useRef<HTMLInputElement>(null);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const { mutate, isLoading } = useApplyCvMutation({ jobId: id });

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (file) {
      console.log("Selected file:", file);
      setSelectedFile(file);    
    }
  };

  const handleApplyClick = () => {
    if (!selectedFile) {
      toast.error("Please select a file first.");
      return;
    }
    const body = uploadFileBodySchema.parse({ file: selectedFile });

    mutate(body, {
      onSuccess: (response) => {
        setSelectedFile(null); 
        toast.success(response.message);
      },
      onError: (error) => {
        console.log(error);
        toast.error(error.message);
      }
    });
  };

  return (
    <section className="container mx-auto py-6">
      <h1 className="text-2xl font-bold mb-4">Job Details</h1>
      <pre className="mb-6">{JSON.stringify(jobDetails, null, 2)}</pre>

      {!isOwner &&
      <div>
        <div className="mb-4">
          <Input type="file" ref={fileInputRef} onChange={handleFileChange} />
        </div>    

        <Button type="button" onClick={handleApplyClick} disabled={isLoading}>
          {isLoading ? "Uploading..." : "Apply"}
        </Button>
      </div>
      }
      
      <GraphqlClientProvider serviceName="cv">
        {isOwner && <CvsList jobId={id} />}        
      </GraphqlClientProvider>
    </section>
  );
};

