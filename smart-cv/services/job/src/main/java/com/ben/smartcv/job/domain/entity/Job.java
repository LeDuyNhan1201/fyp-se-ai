package com.ben.smartcv.job.domain.entity;

import com.ben.smartcv.common.component.BaseDocument;
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
@Document(collection = "jobs")
public class Job extends BaseDocument<String> {

    @NotNull
    @Field("organization_name")
    String organizationName;

    @Field("email")
    String email;

    @Field("phone")
    String phone;

    @NotNull
    @Field("position")
    String position;

    @Field("education")
    String education;

    @NotNull
    @Field("skills")
    String skills;

    @Field("languages")
    String languages;

    @Field("experience")
    String experience;

    @Field("extras")
    String extras;

    @NotNull
    @Field("raw_text")
    String rawText;

}
