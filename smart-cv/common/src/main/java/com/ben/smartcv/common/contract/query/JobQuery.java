package com.ben.smartcv.common.contract.query;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Range;

import static lombok.AccessLevel.PRIVATE;

public class JobQuery {

    @Getter
    @Builder
    @FieldDefaults(level = PRIVATE)
    public static class GetAllJobs {

        String organizationName;

        String position;

        String education;

        String skills;

        String experience;

        Range<Double> salary;

        Integer page;

        Integer size;

    }

}
