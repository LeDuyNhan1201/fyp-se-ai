package com.ben.smartcv.common.contract.event;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

public class CvEvent {

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class CvApplied extends BaseEvent<String> {

        String cvId;

        String fileName;

    }

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class CvProcessed extends BaseEvent<String> {

        String cvId;

        String objectKey;

    }

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class CvDeleted extends BaseEvent<String> {

        String cvId;

        String objectKey;

    }

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class CvFileDeleted extends BaseEvent<String> {

        String cvId;

        String objectKey;

    }

}
