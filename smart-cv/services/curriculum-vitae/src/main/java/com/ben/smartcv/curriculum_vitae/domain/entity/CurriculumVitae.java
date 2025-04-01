package com.ben.smartcv.curriculum_vitae.domain.entity;

import com.ben.smartcv.common.domain.MongoAuditingDocument;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "curriculum_vitae")
public class CurriculumVitae extends MongoAuditingDocument {

    @NotNull
    @Field("name")
    String name;

    @NotNull
    @Field("email")
    String email;

    @Field("phone")
    String phone;

    @NotNull
    @Field("education")
    String education;

    @NotNull
    @Field("skills")
    String skills;

    @Field("experience")
    String experience;

    @NotNull
    @Field("cv_file_name")
    String cvFileName;

    @NotNull
    @Field("raw_text")
    String rawText;

}
