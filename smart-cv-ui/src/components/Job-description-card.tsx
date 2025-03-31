import {
  JobDescriptionSchema
} from "../lib/schemas/job.schema"
import { formatDate } from "@/lib/utils";
import Link from "next/link";
import { Button } from "@/components/ui/button";

export function JobDescriptionCard({ job }: { job: JobDescriptionSchema }) {
  const {
    id,
    organizationName,
    email,
    phone,
    position,
    education,
    skills,
    experience,
    fromSalary,
    toSalary,
    expiredAt,
    createdAt
  } = job;

  return (
    <li className="startup-card group">
      <div className="flex-between">
        <p className="startup_card_date">{createdAt}</p>
        <p className="startup_card_date">{expiredAt}</p>
      </div>

      <div className="flex-between mt-5 gap-5">
        <div className="flex-1">
          <Link href={`/job/${id}`}>
            <h3 className="text-26-semibold line-clamp-1">{organizationName}</h3>
          </Link>
        </div>
      </div>

      <Link href={`/job/${id}`}>
        <p className="startup-card_desc">{skills?.join(", ")}</p>
        <p className="startup-card_desc">{experience?.join(", ")}</p>
        <p className="startup-card_desc">{education?.join(", ")}</p>
        <p className="startup-card_desc">{fromSalary}</p>
        <p className="startup-card_desc">{toSalary}</p>
      </Link>

      <div className="flex-between gap-3 mt-5">
        <p className="text-16-medium">{position}</p>
        <Button className="startup-card_btn" asChild>
          <Link href={`/job/${id}`}>Details</Link>
        </Button>
      </div>
    </li>
  );
};

