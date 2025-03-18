package com.ben.smartcv.common.contract.query;

import lombok.*;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

public class JobQuery {

    @Getter
    @Builder
    @FieldDefaults(level = PRIVATE)
    public static class GetAllJobs {

        Integer offset;

        Integer limit;

    }

}
