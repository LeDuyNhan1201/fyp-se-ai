package com.ben.smartcv.job.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

public class ResponseDto {

    @Getter
    @Builder
    @FieldDefaults(level = PRIVATE)
    public static class JobDescription implements Serializable {

        String id;

        String organizationName;

        String email;

        String phone;

        String position;

        List<String> educations;

        List<String> skills;

        List<String> experiences;

        Double fromSalary;

        Double toSalary;

        OffsetDateTime expiredAt;

        OffsetDateTime createdAt;

        Integer page;

        Integer size;

        Integer totalPages;

    }

}
