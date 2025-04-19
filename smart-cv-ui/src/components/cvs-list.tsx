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
import { toast } from "sonner";
import { useApproveCvMutation } from "@/hooks/mutations/cv.mutation";
import CvTagCard from "./cv-tag-card";

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
          <CvTagCard cv={cv} jobId={jobId} key={cv.id} />
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
