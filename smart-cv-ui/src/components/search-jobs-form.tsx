import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { toast } from "sonner";
import { useFieldArray, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  searchJobsSchema,
  SearchJobsSchema
} from "@/lib/schemas/job.schema";
import { z } from "zod";

interface JobSearchFormProps {
  initValues: SearchJobsSchema
  setFilters: React.Dispatch<React.SetStateAction<
    z.infer<typeof searchJobsSchema>
  >>;
}

const JobSearchForm: React.FC<JobSearchFormProps> =
  ({ initValues, setFilters }) => {
    const { register, control, handleSubmit, reset } = useForm({
      resolver: zodResolver(searchJobsSchema),
      defaultValues: {
        ...initValues,
      },
    });

    const educationsArray = useFieldArray({ control, name: "educations" });
    const skillsArray = useFieldArray({ control, name: "skills" });
    const experiencesArray = useFieldArray({ control, name: "experiences" });

    const onSubmit = (values: SearchJobsSchema) => {
      setFilters((prev) => ({
        ...prev,
        ...values,
        page: 1,
      }));
      reset(values);
      toast("Filters updated!", { description: "Job search filters have been applied." });
    };

    return (
      <form onSubmit={handleSubmit(onSubmit)} className="grid grid-cols-2 gap-4 mb-6">
        <div>
          <Label>Organization</Label>
          <Input {...register("organizationName")} />
        </div>
        <div>
          <Label>Position</Label>
          <Input {...register("position")} />
        </div>

        {/* Educations */}
        <div>
          <Label>Educations</Label>
          {educationsArray.fields.map((field, index) => (
            <div key={field.id} className="flex gap-2">
              <Input {...register(`education.${index}` as const)} />
              <Button type="button" onClick={() => educationsArray.remove(index)}>-</Button>
            </div>
          ))}
          <Button type="button" onClick={() => educationsArray.append("")}>+ Add Education</Button>
        </div>

        {/* Skills */}
        <div>
          <Label>Skills</Label>
          {skillsArray.fields.map((field, index) => (
            <div key={field.id} className="flex gap-2">
              <Input {...register(`skills.${index}` as const)} />
              <Button type="button" onClick={() => skillsArray.remove(index)}>-</Button>
            </div>
          ))}
          <Button type="button" onClick={() => skillsArray.append("")}>+ Add Skill</Button>
        </div>

        {/* Experiences */}
        <div>
          <Label>Experiences</Label>
          {experiencesArray.fields.map((field, index) => (
            <div key={field.id} className="flex gap-2">
              <Input {...register(`experience.${index}` as const)} />
              <Button type="button" onClick={() => experiencesArray.remove(index)}>-</Button>
            </div>
          ))}
          <Button type="button" onClick={() => experiencesArray.append("")}>+ Add Experience</Button>
        </div>

        <div>
          <Label>Salary Range</Label>
          <div className="flex gap-2">
            <Input {...register("fromSalary")} placeholder="Min" type="number" />
            <Input {...register("toSalary")} placeholder="Max" type="number" />
          </div>
        </div>

        <div className="col-span-2 flex justify-end">
          <Button type="submit">Search</Button>
        </div>
      </form>
    );
  };

export default JobSearchForm;

