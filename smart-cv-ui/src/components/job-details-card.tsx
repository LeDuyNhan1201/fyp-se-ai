"use client";

import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { formatDateFromInstant } from "@/lib/utils";

export default function JobDetailsCard({ jobDetails }) {
  const {
    organizationName,
    email,
    phone,
    position,
    skills,
    experiences,
    educations,
    fromSalary,
    toSalary,
    createdAt,
    expiredAt,
  } = jobDetails;

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      <h1 className="text-3xl font-bold text-gray-800">{position}</h1>
      <div className="text-gray-600">
        <p className="text-lg font-semibold">{organizationName}</p>
        <p>{email} • {phone}</p>
      </div>

      <div className="grid grid-cols-2 gap-4 text-sm text-gray-500 mt-4">
        <p><strong>Posted:</strong> {formatDateFromInstant(createdAt)}</p>
        <p><strong>Expires:</strong> {formatDateFromInstant(expiredAt)}</p>
      </div>

      <Card>
        <CardContent className="p-4 space-y-4">
          <div>
            <h2 className="text-lg font-semibold">Required Skills</h2>
            <div className="flex flex-wrap gap-2 mt-2">
              {skills?.slice(0, 6).map((skill, idx) => (
                <Badge key={idx} variant="outline">{skill}</Badge>
              ))}
              {skills?.length > 6 && (
                <span className="text-gray-400 text-xs">+{skills.length - 6} more</span>
              )}
            </div>
          </div>

          <div>
            <h2 className="text-lg font-semibold">Experience</h2>
            <ul className="list-disc list-inside text-gray-700">
              {experiences?.map((exp, idx) => (
                <li key={idx}>{exp}</li>
              ))}
            </ul>
          </div>

          <div>
            <h2 className="text-lg font-semibold">Education</h2>
            <ul className="list-disc list-inside text-gray-700">
              {educations?.map((edu, idx) => (
                <li key={idx}>{edu}</li>
              ))}
            </ul>
          </div>

          <div className="mt-4">
            <h2 className="text-lg font-semibold">Salary</h2>
            <p className="text-gray-700">
              ${fromSalary} - ${toSalary} USD/year
            </p>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
