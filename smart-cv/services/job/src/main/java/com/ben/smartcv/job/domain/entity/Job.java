package com.ben.smartcv.job.domain.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Range;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(indexName = "jobs")
public class Job implements Persistable<String> {

    @Id
    String id;

    @CreatedBy
    @Field(type = FieldType.Keyword)
    String createdBy;

    @CreatedDate
    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    Instant createdAt;

    @LastModifiedBy
    @Field(type = FieldType.Keyword)
    String updatedBy;

    @LastModifiedDate
    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    Instant updatedAt;

    @Field(type = FieldType.Boolean)
    Boolean isDeleted = false;

    @Field(type = FieldType.Keyword)
    String deletedBy;

    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    Instant deletedAt;

    @NotNull
    @Field(name = "organization_name", type = FieldType.Text)
    String organizationName;

    @Field(name = "email", type = FieldType.Text)
    String email;

    @Field(name = "phone", type = FieldType.Text)
    String phone;

    @NotNull
    @Field(name = "position", type = FieldType.Text)
    String position;

    @Field(name = "education", type = FieldType.Keyword)
    List<String> education;

    @NotNull
    @Field(name = "skills", type = FieldType.Keyword)
    List<String> skills;

    @Field(name = "experience", type = FieldType.Keyword)
    List<String> experience;

    @Field(name = "salary", type = FieldType.Double_Range)
    Range<Double> salary;

    @Field(name = "expired_at", type = FieldType.Date, format = DateFormat.basic_date_time)
    Instant expiredAt;

    @NotNull
    @Field(name = "raw_text", type = FieldType.Text)
    String rawText;

    private void delete() {
        deletedAt = Instant.now();
        isDeleted = true;
    }

    @Override
    public boolean isNew() {
        return id == null || (createdAt == null && createdBy == null);
    }

}
