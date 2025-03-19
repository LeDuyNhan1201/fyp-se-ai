package com.ben.smartcv.common.contract.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Range;

import java.time.Instant;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

public class JobModel {

    @Getter
    @Builder
    @FieldDefaults(level = PRIVATE)
    public static class JobDescription {

        String id;

        String organizationName;

        String email;

        String phone;

        String position;

        List<String> education;

        List<String> skills;

        List<String> experience;

        Range<Double> salary;

        Instant expiredAt;

    }

    @Getter
    @Builder
    @FieldDefaults(level = PRIVATE)
    public static class JobDescriptions {

        List<JobDescription> jobDescriptions;

    }

}
