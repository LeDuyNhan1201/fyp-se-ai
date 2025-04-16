"use client";

import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { FormField, FormItem, FormControl, FormMessage } from "@/components/ui/form";

export function DynamicFieldArray({ label, fields, namePrefix, remove, append }) {
  return (
    <div className="md:col-span-2 space-y-4">
      <div className="flex justify-between items-center">
        <Label className="text-lg font-medium">{label}</Label>
        <Button type="button" variant="outline" onClick={() => append("")}>
          + Add {label.slice(0, -1)}
        </Button>
      </div>

      {fields.length === 0 && (
        <p className="text-muted-foreground text-sm">No {label.toLowerCase()} added.</p>
      )}

      <div className="space-y-4">
        {fields.map((field, index) => (
          <div
            key={field.id}
            className="p-4 border rounded-md flex items-center gap-4"
          >
            <FormField
              control={control}
              name={`${namePrefix}.${index}`}
              render={({ field }) => (
                <FormItem className="flex-1">
                  <FormControl>
                    <Input
                      {...field}
                      value={field.value ?? ""}
                      onChange={(e) => field.onChange(e.target.value)}
                      placeholder={`Enter ${label.slice(0, -1).toLowerCase()}`}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />            
            <Button
              type="button"
              variant="destructive"
              size="icon"
              onClick={() => remove(index)}
            >
              &minus;
            </Button>
          </div>
        ))}
      </div>
    </div>
  );
}

