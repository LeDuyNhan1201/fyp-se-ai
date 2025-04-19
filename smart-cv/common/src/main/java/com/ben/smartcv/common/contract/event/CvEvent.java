package com.ben.smartcv.common.contract.event;

import com.ben.smartcv.common.contract.command.BaseCommand;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

public class CvEvent {

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class CvApplied extends BaseEvent<String> {

        String objectKey;

        String jobId;

        String createdBy;

    }

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class CvProcessed extends BaseEvent<String> {

        String objectKey;

        String jobId;

        String createdBy;

    }

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class CvDeleted extends BaseEvent<String> {

        String objectKey;

    }

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class CvFileDeleted extends BaseEvent<String> {

        String objectKey;

    }

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class CvApproved extends BaseCommand<String> {

        String title;

        String content;

        String jobId;

        String receiverId;

        String cvId;

    }

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class CvRenewed extends BaseCommand<String> {

        String cvId;

    }

}
