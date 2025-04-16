"use client";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Dialog,
  DialogTrigger,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogClose,
} from "@/components/ui/dialog"
import JobDetailsCard from "@/components/job-details-card";
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
  const { data: jobDetails } = useGetJobDetailsQuery(getJobDetailsParamsSchema.parse({ id }));

  const { getCurrentUser } = useCurrentUserActions();
  const currentUser = getCurrentUser();
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
    <section className="container mx-auto py-10 space-y-8">
      <JobDetailsCard jobDetails={jobDetails} />

      <div className="flex flex-col items-center justify-center">
        {!isOwner && (
          <div className="w-full max-w-md space-y-4">
            <Input type="file" ref={fileInputRef} onChange={handleFileChange} />
            <Button
              type="button"
              onClick={handleApplyClick}
              disabled={isLoading}
              className="w-full"
            >
              {isLoading ? "Uploading..." : "Apply"}
            </Button>
          </div>
        )}

        {isOwner && (
          <GraphqlClientProvider serviceName="cv">
            <Dialog>
              <DialogTrigger asChild>
                <Button variant="outline" className="w-full max-w-md">
                  Show CV List
                </Button>
              </DialogTrigger>

              <DialogContent className="bg-white max-w-5xl max-h-[90vh] overflow-y-auto">
                <DialogHeader>
                  <DialogTitle>CV Submissions</DialogTitle>
                </DialogHeader>

                <CvsList jobId={id} />
              </DialogContent>
            </Dialog>
          </GraphqlClientProvider>
        )}
      </div>
    </section>
  );
};

