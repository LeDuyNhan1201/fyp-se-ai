"use client";

import { useSearchCvs } from "@/hooks/queries/cv.query";
import {
  searchCvsQuerySchema,
  SearchCvsQuerySchema,
  CvTagSchema
} from "@/lib/schemas/cv.schema";
import { useCallback, useRef, useState } from "react";
import { Card, CardHeader, CardContent, CardFooter } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { Button } from "@/components/ui/button";
import { isImage, isPdf } from "@/lib/utils";

type JobDetailsProps = {
  jobId: string;
};

export default function CvsList({ jobId }: JobDetailsProps) {
  const [filters, setFilters] = useState(() =>
    searchCvsQuerySchema.parse({ jobId })
  );

  const { loading, error, data, loadMore } = useSearchCvs(filters);

  const observer = useRef<IntersectionObserver | null>(null);
  const lastElementRef = useCallback(
    (node: HTMLDivElement) => {
      if (loading) return;
      if (observer.current) observer.current.disconnect();

      observer.current = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && data?.hasNextPage) {
          loadMore();
        }
      });

      if (node) observer.current.observe(node);
    },
    [loadMore, data, loading]
  );

  if (loading) return <Skeleton className="h-48 w-full" />;
  if (error) return <p className="text-red-500">Error loading CVs.</p>;

  const items = data?.items || [];

  return (
    <div className="space-y-6">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {items.map((cv: CvTagSchema) => (
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
          <CardFooter className="flex justify-end">
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
          </CardFooter>
        )}
      </Card>
        ))}
      </div>

      <div
        ref={lastElementRef}
        className="text-center text-gray-500 py-4 text-sm"
      >
        {data?.hasNextPage ? "Loading more..." : "No more results"}
      </div>
    </div>
  );
}
