package com.ben.smartcv.common.contract.query;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

public class CvQuery {

    @Getter
    @Builder
    @FieldDefaults(level = PRIVATE)
    public static class GetAllCvs {

        String cursor;

        Integer limit;

    }

}
