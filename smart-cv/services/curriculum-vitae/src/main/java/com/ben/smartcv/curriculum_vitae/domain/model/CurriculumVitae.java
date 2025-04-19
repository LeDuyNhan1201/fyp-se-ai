package com.ben.smartcv.curriculum_vitae.domain.model;

import com.ben.smartcv.common.contract.dto.Enum;
import com.ben.smartcv.common.domain.MongoAuditingDocument;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "curriculum_vitae")
public class CurriculumVitae extends MongoAuditingDocument {

    @NotNull
    @Field(value = "name", targetType = FieldType.STRING)
    String name;

    @NotNull
    @Field(value = "email", targetType = FieldType.STRING)
    String email;

    @Field(value = "phone", targetType = FieldType.STRING)
    String phone;

    @NotNull
    @Field(value = "educations")
    List<String> educations;

    @NotNull
    @Field(value = "skills")
    List<String> skills;

    @Field(value = "experiences")
    List<String> experiences;

    @NotNull
    @Field(value = "object_key", targetType = FieldType.STRING)
    String objectKey;

    @NotNull
    @Field(value = "job_id", targetType = FieldType.STRING)
    String jobId;

    @NotNull
    @Field(value = "score", targetType = FieldType.DOUBLE)
    Double score;

    @NotNull
    @Field(value = "status", targetType = FieldType.STRING)
    Enum.CvStatus status;

}
