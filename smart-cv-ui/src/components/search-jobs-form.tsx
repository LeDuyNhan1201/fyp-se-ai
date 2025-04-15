import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
  FormControl
} from "@/components/ui/form";
import { toast } from "sonner";
import { useFieldArray, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  searchJobsSchema,
  SearchJobsSchema
} from "@/lib/schemas/job.schema";

interface JobSearchFormProps {
  initValues: SearchJobsSchema;
  setFilters: React.Dispatch<React.SetStateAction<SearchJobsSchema>>;
}

const JobSearchForm: React.FC<JobSearchFormProps> = ({ initValues, setFilters }) => {
  const searchForm = useForm<SearchJobsSchema>({
    resolver: zodResolver(searchJobsSchema),
    defaultValues: {
      ...initValues,
    },
  });

  const {
    control,
    handleSubmit,
    reset,
  } = searchForm;

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
    <Form {...searchForm}>
      <form onSubmit={handleSubmit(onSubmit)} className="grid grid-cols-2 gap-4 mb-6">
        
        {/* Organization */}
        <FormField
          control={control}
          name="organizationName"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Organization</FormLabel>
              <FormControl>
                <Input {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        {/* Position */}
        <FormField
          control={control}
          name="position"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Position</FormLabel>
              <FormControl>
                <Input {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        {/* Educations */}
        <div className="col-span-2">
          <Label>Educations</Label>
          {educationsArray.fields.map((field, index) => (
            <FormField
              key={field.id}
              control={control}
              name={`educations.${index}`}
              render={({ field }) => (
                <FormItem className="flex gap-2 items-center">
                  <FormControl>
                    <Input {...field} />
                  </FormControl>
                  <Button type="button" onClick={() => educationsArray.remove(index)}>-</Button>
                  <FormMessage />
                </FormItem>
              )}
            />
          ))}
          <Button type="button" onClick={() => educationsArray.append("")}>+ Add Education</Button>
        </div>

        {/* Skills */}
        <div className="col-span-2">
          <Label>Skills</Label>
          {skillsArray.fields.map((field, index) => (
            <FormField
              key={field.id}
              control={control}
              name={`skills.${index}`}
              render={({ field }) => (
                <FormItem className="flex gap-2 items-center">
                  <FormControl>
                    <Input {...field} />
                  </FormControl>
                  <Button type="button" onClick={() => skillsArray.remove(index)}>-</Button>
                  <FormMessage />
                </FormItem>
              )}
            />
          ))}
          <Button type="button" onClick={() => skillsArray.append("")}>+ Add Skill</Button>
        </div>

        {/* Experiences */}
        <div className="col-span-2">
          <Label>Experiences</Label>
          {experiencesArray.fields.map((field, index) => (
            <FormField
              key={field.id}
              control={control}
              name={`experiences.${index}`}
              render={({ field }) => (
                <FormItem className="flex gap-2 items-center">
                  <FormControl>
                    <Input {...field} />
                  </FormControl>
                  <Button type="button" onClick={() => experiencesArray.remove(index)}>-</Button>
                  <FormMessage />
                </FormItem>
              )}
            />
          ))}
          <Button type="button" onClick={() => experiencesArray.append("")}>+ Add Experience</Button>
        </div>

        {/* Salary Range */}
        <FormField
          control={control}
          name="fromSalary"
          render={({ field }) => (
            <FormItem>
              <FormLabel>From Salary</FormLabel>
              <FormControl>
                <Input
                  type="number"
                  value={field.value ?? ""}
                  onChange={(e) => field.onChange(e.target.value === "" ? undefined : Number(e.target.value))}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={control}
          name="toSalary"
          render={({ field }) => (
            <FormItem>
              <FormLabel>To Salary</FormLabel>
              <FormControl>
                <Input
                  type="number"
                  value={field.value ?? ""}
                  onChange={(e) => field.onChange(e.target.value === "" ? undefined : Number(e.target.value))}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <div className="col-span-2 flex justify-end">
          <Button type="submit">Search</Button>
        </div>
      </form>
    </Form>
  );
};

export default JobSearchForm;
