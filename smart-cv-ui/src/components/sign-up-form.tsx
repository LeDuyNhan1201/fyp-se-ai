'use client';

import Link from "next/link";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";
import { z } from "zod";
import { useRouter } from "next/navigation";
import {
  signUpBodySchema,
  SignUpBodySchema,
  signUpErrorResponseSchema,
} from "@/lib/schemas/auth.schema";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from "@/components/ui/form";
import { Checkbox } from "@/components/ui/checkbox";
import { 
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardDescription,
} from "@/components/ui/card";
import { useMutation } from "@tanstack/react-query";
import { useSignUpMutation } from "@/hooks/mutations/auth.mutation";

export default function SignUpForm() {
  const signUpForm = useForm<SignUpBodySchema>({
    resolver: zodResolver(signUpBodySchema),
    defaultValues: {
      firstName: "",
      lastName: "",
      email: "",
      password: "",
      confirmationPassword: "",
      acceptTerms: false,
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
  } = signUpForm;

  const router = useRouter();
  const mutation = useSignUpMutation();
  const onSubmit = (data: SignUpBodySchema) => {
    mutation.mutate(data, {
      onSuccess: (response) => {
        router.push("/sign-in");
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
    <Card className="bg-white">
      <CardHeader>
        <CardTitle>Welcome to Smart CV</CardTitle>
        <CardDescription>Upload your resume in one-click.</CardDescription>
      </CardHeader>
      <CardContent>
        <div className="grid w-full items-center gap-4">
        <Form {...signUpForm}>
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={control}
              name="firstName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>First name</FormLabel>
                  <FormControl>
                    <Input type="text" placeholder="Enter your first name" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={control}
              name="lastName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Last name</FormLabel>
                  <FormControl>
                    <Input type="text" placeholder="Enter your last name" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={control}
              name="email"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Email</FormLabel>
                  <FormControl>
                    <Input type="email" placeholder="Enter your email" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={control}
              name="password"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Password</FormLabel>
                  <FormControl>
                    <Input type="password" placeholder="Enter your password" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={control}
              name="confirmationPassword"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Confirmation password</FormLabel>
                  <FormControl>
                    <Input type="password" placeholder="Enter your password" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={control}
              name="acceptTerms"
              render={({ field }) => (
                <FormItem className="flex items-center space-x-3">
                  <FormControl>
                    <Checkbox
                      checked={field.value}
                      onCheckedChange={field.onChange}
                    />
                  </FormControl>
                  <FormLabel className="text-sm">
                    I accept the{" "}
                    <Link href="/terms" className="underline text-blue-600">
                      Terms and Conditions
                    </Link>
                  </FormLabel>
                  <FormMessage />
                </FormItem>
              )}
            />

            <Button type="submit" disabled={mutation.isPending} className="w-40 mx-auto block">
              {mutation.isPending ? "Signing up..." : "Sign Up"}
            </Button>

            <Link href="/sign-in" className="mx-auto block text-center text-xs">
              Already have an account? Sign in
            </Link>

          </form>
        </Form>
        </div>
      </CardContent>
    </Card>
  );
}
