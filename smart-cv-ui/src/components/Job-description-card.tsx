import {
  JobDescriptionSchema
} from "../lib/schemas/job.schema"
import { formatDate } from "@/lib/utils";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { CalendarDays, Clock } from "lucide-react";

export function JobDescriptionCard({ job }: { job: JobDescriptionSchema }) {
  const {
    id,
    organizationName,
    email,
    phone,
    position,
    educations,
    skills,
    experiences,
    fromSalary,
    toSalary,
    expiredAt,
    createdAt
  } = job;

  return (
    <li className="startup-card group p-5 rounded-lg shadow-md hover:shadow-lg transition">
      <div className="flex justify-between text-sm text-gray-500">
        <p className="flex items-center gap-1">
          <CalendarDays className="w-4 h-4" />
          {formatDate(createdAt)}
        </p>
        <p className="flex items-center gap-1">
          <Clock className="w-4 h-4" />
          {formatDate(expiredAt)}
        </p>
      </div>

      <div className="mt-4">
        <Link href={`/job/${id}`}>
          <h3 className="text-xl font-semibold text-gray-800 line-clamp-1 hover:underline">
            {organizationName}
          </h3>
        </Link>
        <p className="text-gray-600 text-base mt-1 line-clamp-1">{position}</p>
      </div>

      <div className="mt-4 space-y-1 text-gray-700 text-sm">
        {skills?.length > 0 && (
          <p className="line-clamp-1">
            <span className="font-medium">Skills:</span> {skills.slice(0, 3).join(", ")}
            {skills.length > 3 && '...'}
          </p>
        )}
        <p>
          <span className="font-medium">Salary:</span> {fromSalary} - {toSalary} USD/year
        </p>
      </div>

      <div className="mt-5 text-right">
        <Button className="startup-card_btn" asChild>
          <Link href={`/job/${id}`}>View Details</Link>
        </Button>
      </div>
    </li>
  );
};

