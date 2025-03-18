package com.ben.smartcv.job.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Range;

import java.util.Date;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

public class ResponseDto {

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

        Date expiredAt;

    }

}
