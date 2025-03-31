import { useSearchCvs } from "@/hooks/queries/cv.query";
import {
  searchCvsSchema,
  SearchCvsSchema
} from "@/lib/schemas/cv.schema";
import { useCallback, useRef, useState } from "react";
import { Skeleton } from "./ui/skeleton";
import { Card, CardContent, CardHeader } from "./ui/card";

type JobDetailsProps = {
  jobId: string;
};

export default function CvsList({
  jobId,
}: JobDetailsProps) {
  const [filters, setFilters] = useState<SearchCvsSchema>(
    searchCvsSchema.parse({
      jobId: jobId
    })
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
    [loadMore, data]
  );

  if (loading) return <Skeleton className="h-48 w-full" />;
  if (error) return <p className="text-red-500">Error loading CVs.</p>;

  const items = data?.items || [];

  return (
    <div className="space-y-4">
      {items.map((cv: any) => (
        <Card key={cv.id} className="p-4">
          <CardHeader>
            <h2 className="text-lg font-semibold">{cv.email}</h2>
          </CardHeader>
          <CardContent>
            <p><strong>Phone:</strong> {cv.phone}</p>
            <p><strong>Education:</strong> {cv.education.join(", ")}</p>
            <p><strong>Skills:</strong> {cv.skills.join(", ")}</p>
            <p><strong>Experience:</strong> {cv.experience}</p>
          </CardContent>
        </Card>
      ))}
      <div ref={lastElementRef} className="text-center text-gray-500 py-4">
        {data?.hasNextPage ? "Loading more..." : "No more results"}
      </div>
    </div>
  );
}
