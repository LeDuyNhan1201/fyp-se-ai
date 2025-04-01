package com.ben.smartcv.job.domain.entity;

import com.ben.smartcv.common.domain.AuditingEntity;
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

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "organization_name", nullable = false)
    String organizationName;

    @Column(name = "email")
    String email;

    @Column(name = "phone")
    String phone;

    @Column(name = "position", nullable = false)
    String position;

    @Column(name = "educations")
    List<String> educations;

    @Column(name = "skills", nullable = false)
    List<String> skills;

    @Column(name = "experiences")
    List<String> experiences;

    @Column(name = "from_salary", nullable = false)
    Double fromSalary;

    @Column(name = "to_salary", nullable = false)
    Double toSalary;

    @Column(name = "expired_at")
    Instant expiredAt;

    @Column(name = "raw_text", nullable = false, columnDefinition = "TEXT")
    String rawText;

}
