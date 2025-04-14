"use client";

import { useCreateJobMutation } from "@/hooks/mutations/job.mutation";
import { createJobSchema, CreateJobSchema } from "@/lib/schemas/job.schema";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { toast } from "sonner";
import { Form, FormField, FormItem, FormLabel, FormMessage } from "./ui/form";
import { Input } from "./ui/input";
import { Button } from "./ui/button";
import { mapFieldErrorToFormError } from "@/lib/utils";

export default function CreateJobForm() {
  const createJobForm = useForm<CreateJobSchema>({
    resolver: zodResolver(createJobSchema),
    defaultValues: {
      organizationName: "",
      position: "",
      fromSalary: 0,
      toSalary: 0,
      expiredAt: "",
      details: "",
    },
  });

  const {
    control,
    handleSubmit,
    setValue,
    setError,
    watch,
    reset,
    formState: { errors },
  } = createJobForm;

  const mutation = useCreateJobMutation();

  const onSubmit = (data: CreateJobSchema) => {
    mutation.mutate(data, {
      onSuccess: (response) => {
        toast.success(response.message);
      },
      onError: (error) => {
        console.log(error);
        toast.error(error.message);
        switch (error.errorCode) {
          case "common/validation-error":
            mapFieldErrorToFormError(setError, error.errors);
        }
      }
    });
  };

  return (
    <Form {...createJobForm}>
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <FormField
          control={control}
          name="organizationName"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Organization Name</FormLabel>
              <Input {...field} placeholder="Enter organization name" />
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={control}
          name="position"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Position</FormLabel>
              <Input {...field} placeholder="Enter job position" />
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={control}
          name="fromSalary"
          render={({ field }) => (
            <FormItem>
              <FormLabel>From Salary</FormLabel>
              <Input
                type="number"
                {...field}
                onChange={(e) => field.onChange(+e.target.value)} // ép kiểu về number
              />
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={control}
          name="toSalary"
          render={({ field }) => (
            <FormItem>
              <FormLabel>To Salary</FormLabel>
              <Input
                type="number"
                {...field}
                onChange={(e) => field.onChange(+e.target.value)} // ép kiểu về number
              />
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={control}
          name="expiredAt"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Expiration Date</FormLabel>
              <Input type="datetime-local" {...field} />
              <FormMessage />
            </FormItem>
          )}
        />
        
        <FormField
          control={control}
          name="requirements"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Job Requirements</FormLabel>
              <textarea
                {...field}
                maxLength={500}
                placeholder="Enter requirements (max 500 chars)"
                className="border rounded p-2 w-full"
              />
              <FormMessage />
            </FormItem>
          )}
        />

        <Button type="submit" disabled={mutation.isPending}>
          {mutation.isPending ? "Creating..." : "Create Job"}
        </Button>
      </form>
    </Form>
  );
}
