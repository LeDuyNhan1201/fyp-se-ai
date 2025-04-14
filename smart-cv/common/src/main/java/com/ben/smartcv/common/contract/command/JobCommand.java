package com.ben.smartcv.common.contract.command;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

public class JobCommand {

    @Getter
    @SuperBuilder
    @FieldDefaults(level = PRIVATE)
    public static class CreateJob extends BaseCommand<String> {

        String createdBy;

        String organizationName;

        String position;

        Instant expiredAt;

        Double fromSalary;

        Double toSalary;

        String requirements;

    }

    @Getter
    @SuperBuilder
    @FieldDefaults(level = PRIVATE)
    public static class RollbackCreateJob extends BaseCommand<String> {

        String jobId;

    }

    @Getter
    @SuperBuilder
    @FieldDefaults(level = PRIVATE)
    public static class RollbackUpdateJob extends BaseCommand<String> {

        String jobId;

    }

}
