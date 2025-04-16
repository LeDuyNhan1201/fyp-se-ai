'use client';

import Link from "next/link";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";
import { z } from "zod";
import { useRouter } from "next/navigation";
import {
  signInBodySchema,
  SignInBodySchema,
  signInErrorResponseSchema,
} from "@/lib/schemas/auth.schema";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Form,
  FormField,
  FormItem,
  FormControl,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { 
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardDescription,
} from "@/components/ui/card";
import { useMutation } from "@tanstack/react-query";
import { useSignInMutation } from "@/hooks/mutations/auth.mutation";

export default function SignInForm() {
  const signInForm = useForm<SignInBodySchema>({
    resolver: zodResolver(signInBodySchema),
    defaultValues: {
      email: "",
      password: "",
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
  } = signInForm;

  const router = useRouter();
  const mutation = useSignInMutation();
  const onSubmit = (data: SignInBodySchema) => {
    mutation.mutate(data, {
      onSuccess: (response) => {
        router.push("/");
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
        <Form {...signInForm}>
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
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

            <Button type="submit" disabled={mutation.isPending} className="w-40 mx-auto block">
              {mutation.isPending ? "Signing in..." : "Sign In"}
            </Button>

            <Link href="/sign-up" className="mx-auto block text-center text-xs">
              Don't have an account? Sign up
            </Link>
          </form>
        </Form>
        </div>
      </CardContent>
    </Card>
  );
}
