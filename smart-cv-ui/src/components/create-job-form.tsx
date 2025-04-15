"use client";

import { useCreateJobMutation } from "@/hooks/mutations/job.mutation";
import { createJobBodySchema, CreateJobBodySchema } from "@/lib/schemas/job.schema";
import { zodResolver } from "@hookform/resolvers/zod";
import { format } from "date-fns"
import { useForm } from "react-hook-form";
import { toast } from "sonner";
import { Form, FormField, FormItem, FormLabel, FormMessage, FormControl } from "./ui/form";
import { Input } from "./ui/input";
import { Button } from "./ui/button";
import { Textarea } from "./ui/textarea";
import { Popover, PopoverContent, PopoverTrigger } from "./ui/popover";
import { Calendar } from "./ui/calendar";
import { CalendarIcon } from "lucide-react"
import { cn, mapFieldErrorToFormError } from "@/lib/utils";

export default function CreateJobForm() {
  const createJobForm = useForm<CreateJobBodySchema>({
    resolver: zodResolver(createJobBodySchema),
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

  const onSubmit = (data: CreateJobBodySchema) => {
    mutation.mutate(data, {
      onSuccess: (response) => {
        toast.success(ressponse.message);
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
      <form onSubmit={handleSubmit(onSubmit)} className="w-2/3 space-y-6">
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
                onChange={(e) => field.onChange(+e.target.value)} 
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
                onChange={(e) => field.onChange(+e.target.value)} 
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
              <Popover>
                <PopoverTrigger asChild>
                  <FormControl>
                    <Button
                      variant={"outline"}
                      className={cn(
                        "w-[240px] pl-3 text-left font-normal",
                        !field.value && "text-muted-foreground"
                      )}
                    >
                      {field.value ? (
                        format(field.value, "PPP")
                      ) : (
                        <span>Pick a date</span>
                      )}
                      <CalendarIcon className="ml-auto h-4 w-4 opacity-50" />
                    </Button>
                  </FormControl>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0" align="start">
                  <Calendar className="bg-white"
                    mode="single"
                    selected={field.value}
                    onSelect={field.onChange}
                    disabled={(date) =>
                      date < new Date("1900-01-01")
                    }
                    initialFocus
                  />
                </PopoverContent>
              </Popover>
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
              <Textarea
                {...field}
                maxLength={500}
                placeholder="Enter requirements (max 500 chars)"
                className="resize-none"
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
