import { useCreateJobMutation } from "@/hooks/mutations/job.mutation";
import { createJobSchema, CreateJobSchema } from "@/lib/schemas/job.schema";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { toast } from "sonner";
import { FormField, FormItem, FormLabel, FormMessage } from "./ui/form";
import { Input } from "./ui/input";
import { Button } from "./ui/button";
import { mapFieldErrorToFormError } from "@/lib/utils";

export default function CreateJobForm() {
  const {
    control,
    handleSubmit,
    setValue,
    setError,
    watch,
    reset,
    formState: { errors },
  } = useForm<CreateJobSchema>({
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

  const mutation = useCreateJobMutation();

  const onSubmit = (data: CreateJobSchema) => {
    mutation.mutate(data, {
      onSuccess: (response) => {
        toast.success(response.message);
        reset();
      },
      onError: (error) => {
        console.log(error);
        toast.error(error.message);
        switch (error.type) {
          case "ValidationError":
            mapFieldErrorToFormError(setError, error.errors);
        }
      }
    });
  };

  return (
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
            <Input type="number" {...field} />
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
            <Input type="number" {...field} />
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
            <textarea {...field} placeholder="Enter requirements (max 300 chars)" />
            <FormMessage />
          </FormItem>
        )}
      />

      <Button type="submit" disabled={mutation.isPending}>
        {mutation.isPending ? "Creating..." : "Create Job"}
      </Button>
    </form>
  );
}
