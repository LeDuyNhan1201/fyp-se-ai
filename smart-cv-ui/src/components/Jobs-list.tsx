"use client";

import React, { useState } from "react";
import { useSearchJobs } from "@/hooks/queries/search-jobs.query";
import { JobDescriptionCard } from "@/components/Job-description-card";
import {
  searchJobQuerySchema,
  SearchJobQuerySchema
} from "@/lib/schemas/job.schema";
import {
  Pagination,
  PaginationContent,
  PaginationItem,
  PaginationNext,
  PaginationPrevious
} from "@/components/ui/pagination";
import JobSearchForm from "./search-jobs-form";

const JobList = () => {
  const [filters, setFilters] = useState<SearchJobQuerySchema>(
    searchJobQuerySchema.parse({})
  );

  const { loading, error, data, goToPage } = useSearchJobs(filters);

  if (loading && !data) return <p>Loading jobs...</p>;
  if (error) return <p>Error loading jobs: {error.message}</p>;

  return (
    <>
      <JobSearchForm initValues={filters} setFilters={setFilters} />
      <ul className="mt-7 card_grid">
        {data?.items && data.items.length > 0 ? (
          data.items.map(
            (job) => <JobDescriptionCard key={job.id} job={job} />
          )
        ) : (
          <p className="no-results">No startups found</p>
        )}
      </ul>

      <div className="flex justify-center mt-6">
        <Pagination>
          <PaginationContent>
            <PaginationItem>
              <PaginationPrevious
                onClick={() => goToPage((data?.page) ? data.page - 1 : 1)}
                className={
                  data?.page === 1 || loading
                    ? "pointer-events-none opacity-50" : "cursor-pointer"
                }
              />
            </PaginationItem>

            {Array.from(
              { length: data?.totalPages || 1 },
              (_, i) => i + 1).map((page) => (
                <PaginationItem key={page}>
                  <span
                    className={`cursor-pointer px-3 py-1 rounded 
                    ${data?.page === page
                        ? "bg-gray-300"
                        : "hover:bg-gray-200"}`
                    }
                    onClick={() => goToPage(page)}
                  >
                    {page}
                  </span>
                </PaginationItem>
              ))}

            <PaginationItem>
              <PaginationNext
                onClick={() => goToPage((data?.page) ? data.page + 1 : 1)}
                className={
                  data?.page === data?.totalPages || loading
                    ? "pointer-events-none opacity-50" : "cursor-pointer"
                }
              />
            </PaginationItem>
          </PaginationContent>
        </Pagination>
      </div>
    </>
  );
};

export default JobList;
