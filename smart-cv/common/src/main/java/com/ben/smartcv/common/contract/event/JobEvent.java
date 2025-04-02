package com.ben.smartcv.common.contract.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

import static lombok.AccessLevel.PRIVATE;

public class JobEvent {

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class JobCreated extends BaseEvent<String> {

        String organizationName;

        String position;

        Instant expiredAt;

        Double fromSalary;

        Double toSalary;

        String requirements;

    }

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class JobDeleted extends BaseEvent<String> {

        String jobId;

    }

}
