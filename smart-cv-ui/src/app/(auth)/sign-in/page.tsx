import SignInForm from '@/components/sign-in-form';

export default function Page() {
  return (
    <div className="min-h-screen flex items-center justify-center px-4">
      <div className="w-full max-w-xs">
        <SignInForm />
      </div>
    </div>
  );
}
