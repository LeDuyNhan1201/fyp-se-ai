package com.ben.smartcv.common.contract.query;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Range;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

public class JobQuery {

    @Getter
    @Builder
    @FieldDefaults(level = PRIVATE)
    public static class Search {

        String organizationName;

        String position;

        List<String> educations;

        List<String> skills;

        List<String> experiences;

        Range<Double> salary;

        Integer page;

        Integer size;

    }

    @Getter
    @Builder
    @FieldDefaults(level = PRIVATE)
    public static class GetById {

        String id;

    }

}
