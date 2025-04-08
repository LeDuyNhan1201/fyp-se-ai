package com.ben.smartcv.curriculum_vitae.domain.entity;

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
    @Field(value = "educations", targetType = FieldType.ARRAY)
    List<String> educations;

    @NotNull
    @Field(value = "skills", targetType = FieldType.ARRAY)
    List<String> skills;

    @Field(value = "experiences", targetType = FieldType.ARRAY)
    List<String> experiences;

    @NotNull
    @Field(value = "cv_file_name", targetType = FieldType.STRING)
    String cvFileName;

    @NotNull
    @Field(value = "raw_text", targetType = FieldType.STRING)
    String rawText;

}
