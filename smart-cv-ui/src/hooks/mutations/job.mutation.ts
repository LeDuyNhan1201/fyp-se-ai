import { useMutation } from "@tanstack/react-query";
import {
  CreateJobBodySchema,
  CreateJobResponseSchema,
  JobErrorResponseSchema
} from "../../lib/schemas/job.schema"
import { createJob } from "@/lib/apis/job.api";
import { isAxiosError } from "axios";

export function useCreateJobMutation() {
  return useMutation<
    CreateJobResponseSchema,
    JobErrorResponseSchema,
    CreateJobBodySchema
  >({
    mutationKey: ["job", "create"],
    mutationFn: (body) => createJob(body),
    throwOnError: (error) => isAxiosError(error),
  });
}
