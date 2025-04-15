package com.ben.smartcv.job.domain.model;

import com.ben.smartcv.common.domain.AuditingEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "job")
public class MasterJob extends AuditingEntity {

    @JsonProperty(value = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @JsonProperty(value = "organization_name")
    @Column(name = "organization_name", nullable = false)
    String organizationName;

    @JsonProperty(value = "email")
    @Column(name = "email")
    String email;

    @JsonProperty(value = "phone")
    @Column(name = "phone")
    String phone;

    @JsonProperty(value = "position")
    @Column(name = "position", nullable = false)
    String position;

    @JsonProperty(value = "educations")
    @Column(name = "educations")
    List<String> educations;

    @JsonProperty(value = "skills")
    @Column(name = "skills", nullable = false)
    List<String> skills;

    @JsonProperty(value = "experiences")
    @Column(name = "experiences")
    List<String> experiences;

    @JsonProperty(value = "from_salary")
    @Column(name = "from_salary", nullable = false)
    Double fromSalary;

    @JsonProperty(value = "to_salary")
    @Column(name = "to_salary", nullable = false)
    Double toSalary;

    @JsonProperty(value = "expired_at")
    @Column(name = "expired_at")
    Instant expiredAt;

    @JsonProperty(value = "requirements")
    @Column(name = "requirements", nullable = false, columnDefinition = "TEXT")
    String requirements;

}
