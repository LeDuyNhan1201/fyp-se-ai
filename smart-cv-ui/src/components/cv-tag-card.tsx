"use client";

import {
  CvTagSchema
} from "@/lib/schemas/cv.schema";
import { Card, CardHeader, CardContent, CardFooter } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { isImage, isPdf } from "@/lib/utils";
import { toast } from "sonner";
import { useApproveCvMutation } from "@/hooks/mutations/cv.mutation";

type CvTagProps = {
  cv: CvTagSchema;
  jobId: string;
};

export default function CvTagCard({ cv, jobId }: CvTagProps) {
  const { mutate: approveCv, isPending } = useApproveCvMutation({ jobId });
  const handleSendMail = (receiverId: string, cvId: string) => {
    approveCv(
      {
        title: null,
        content: null,
        receiverId,
        cvId,
      }, {
      onSuccess: (response) => {
        toast.success(response.message);
        cv.status = "Approved";
      },
      onError: (error) => {
        toast.error(error.message);
      },
    }
    );
  };

  return (
    <Card
      key={cv.id}
      className="shadow-md rounded-xl border border-gray-200 h-full flex flex-col justify-between"
    >
      <CardHeader>
        <p className="text-xl font-semibold text-gray-800 truncate">
          <strong>Score:</strong> {cv.score?.toFixed(2) ?? "N/A"}
        </p>
      </CardHeader>

      <CardContent>
        {cv.downloadUrl && (
          <div className="border rounded-lg p-2 bg-gray-50">
            {isImage(cv.objectKey) && (
              <img
                src={cv.downloadUrl}
                alt="CV Preview"
                className="w-full h-40 rounded object-contain"
              />
            )}

            {isPdf(cv.objectKey) && (
              <iframe
                src={cv.downloadUrl}
                className="w-full h-40 rounded"
                title="CV PDF Preview"
              />
            )}
          </div>
        )}
      </CardContent>

      {cv.downloadUrl && (
        <CardFooter className="flex justify-between items-center w-full">
          <a
            href={cv.downloadUrl}
            target="_blank"
            rel="noopener noreferrer"
            download
          >
            <Button variant="outline" size="sm">
              Download
            </Button>
          </a>

          {cv.status === "Pending" ? (
            <Button
              onClick={() => handleSendMail(cv.createdBy, cv.id)}
              variant="outline"
              size="sm"
              disabled={isPending}
            >
              {isPending ? "Sending..." : "Approve"}
            </Button>
          ) : (
            <p className="text-sm text-green-600">{cv.status}</p>
          )}
        </CardFooter>
      )}

    </Card>
  );
}
