package com.ben.smartcv.job.domain.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Range;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(indexName = "job")
@Setting(settingPath = "es-settings/analysis-config.json")
public class SlaveJob {

    @Id
    String id;

    @Field(name = "created_by", type = FieldType.Keyword)
    String createdBy;

    @Field(name = "created_at", type = FieldType.Date,
            format = DateFormat.basic_date_time)
    Instant createdAt;

    @Field(name = "updated_by", type = FieldType.Keyword)
    String updatedBy;

    @Field(name = "updated_at", type = FieldType.Date,
            format = DateFormat.basic_date_time)
    Instant updatedAt;

    @Field(name = "is_deleted", type = FieldType.Boolean)
    Boolean isDeleted = false;

    @Field(name = "deleted_by", type = FieldType.Keyword)
    String deletedBy;

    @Field(name = "deleted_at", type = FieldType.Date,
            format = DateFormat.basic_date_time)
    Instant deletedAt;

    @NotNull
    @MultiField(
            mainField = @Field(name = "organization_name", type = FieldType.Text, fielddata = true,
                    analyzer = "autocomplete_index",
                    searchAnalyzer = "autocomplete_search"),
            otherFields = {
                    @InnerField(suffix = "verbatim", type = FieldType.Keyword)
            }
    )
    String organizationName;

    @Field(name = "email", type = FieldType.Text)
    String email;

    @Field(name = "phone", type = FieldType.Text)
    String phone;

    @NotNull
    @Field(name = "position", type = FieldType.Text,
            analyzer = "autocomplete_index",
            searchAnalyzer = "autocomplete_search")
    String position;

    @Field(name = "educations", type = FieldType.Keyword)
    List<String> educations;

    @NotNull
    @Field(name = "skills", type = FieldType.Keyword)
    List<String> skills;

    @Field(name = "experiences", type = FieldType.Keyword)
    List<String> experiences;

    @Field(name = "salary", type = FieldType.Double_Range)
    Range<Double> salary;

    @Field(name = "expired_at", type = FieldType.Date,
            format = DateFormat.basic_date_time)
    Instant expiredAt;

    @NotNull
    @Field(name = "raw_text", type = FieldType.Text)
    String rawText;

    public static SlaveJob sync(MasterJob masterJob) {
        SlaveJob slaveJob = new SlaveJob();
        slaveJob.setId(masterJob.getId());
        slaveJob.setCreatedBy(masterJob.getCreatedBy());
        slaveJob.setCreatedAt(masterJob.getCreatedAt());
        slaveJob.setUpdatedBy(masterJob.getUpdatedBy());
        slaveJob.setUpdatedAt(masterJob.getUpdatedAt());
        slaveJob.setIsDeleted(masterJob.isDeleted());
        slaveJob.setDeletedBy(masterJob.getDeletedBy());
        slaveJob.setDeletedAt(masterJob.getDeletedAt());
        slaveJob.setOrganizationName(masterJob.getOrganizationName());
        slaveJob.setEmail(masterJob.getEmail());
        slaveJob.setPhone(masterJob.getPhone());
        slaveJob.setPosition(masterJob.getPosition());
        slaveJob.setEducations(masterJob.getEducations());
        slaveJob.setSkills(masterJob.getSkills());
        slaveJob.setExperiences(masterJob.getExperiences());
        slaveJob.setSalary(Range.closed(masterJob.getFromSalary(), masterJob.getToSalary()));
        slaveJob.setExpiredAt(masterJob.getExpiredAt());
        slaveJob.setRawText(masterJob.getRawText());
        return slaveJob;
    }

}
