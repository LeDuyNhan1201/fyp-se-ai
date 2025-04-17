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
      <form 
        onSubmit={handleSubmit(onSubmit)} 
        className="startup-form"
      >
        <FormField
          control={control}
          name="organizationName"
          render={({ field }) => (
            <FormItem>
              <FormLabel 
                className="startup-form_label"
              >Organization Name</FormLabel>
              <Input 
                className="startup-form_input"
                {...field} placeholder="Enter organization name" />
              <FormMessage className="startup-form_error"/>
            </FormItem>
          )}
        />

        <FormField
          control={control}
          name="position"
          render={({ field }) => (
            <FormItem>
              <FormLabel 
                className="startup-form_label"
              >Position</FormLabel>
              <Input 
                className="startup-form_input"
                {...field} placeholder="Enter job position" />
              <FormMessage className="startup-form_error"/>
            </FormItem>
          )}
        />

        <FormField
          control={control}
          name="fromSalary"
          render={({ field }) => (
            <FormItem>
              <FormLabel
                className="startup-form_label"
              >From Salary</FormLabel>
              <Input
                className="startup-form_input"
                type="number"
                {...field}
                onChange={(e) => field.onChange(+e.target.value)} 
              />
              <FormMessage className="startup-form_error"/>
            </FormItem>
          )}
        />

        <FormField
          control={control}
          name="toSalary"
          render={({ field }) => (
            <FormItem>
              <FormLabel 
                className="startup-form_label"
              >To Salary</FormLabel>
              <Input
                className="startup-form_input"
                type="number"
                {...field}
                onChange={(e) => field.onChange(+e.target.value)} 
              />
              <FormMessage className="startup-form_error"/>
            </FormItem>
          )}
        />

        <FormField
          control={control}
          name="expiredAt"
          render={({ field }) => (
            <FormItem>
              <FormLabel 
                className="startup-form_label"
              >Expiration Date</FormLabel>
              <Popover>
                <PopoverTrigger asChild>
                  <FormControl>
                    <Button
                      variant={"outline"}
                      className={cn(
                        "startup-form_input w-[300px] pl-3 text-left font-normal",
                        !field.value && "startup-form_input text-muted-foreground"
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
                  <Calendar 
                    className="bg-white"
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
              <FormMessage className="startup-form_error"/>
            </FormItem>
          )}
        />
        
        <FormField
          control={control}
          name="requirements"
          render={({ field }) => (
            <FormItem>
              <FormLabel 
                className="startup-form_label"
              >Job Requirements</FormLabel>
              <Textarea
                {...field}
                maxLength={500}
                placeholder="Enter requirements (max 500 chars)"
                className="startup-form_textarea resize-none h-40"
              />
              <p className="text-right text-sm text-muted-foreground">
                {field.value?.length || 0}/500
              </p>
              <FormMessage className="startup-form_error"/>
            </FormItem>
          )}
        />

        <div className="flex justify-center mt-4">
          <Button type="submit" 
            className="startup-form_btn"
            disabled={mutation.isPending}>
            {mutation.isPending ? "Creating..." : "Create Job"}
          </Button>
        </div>
      </form>
    </Form>
  );
}
