package com.ben.smartcv.curriculum_vitae.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;

public class ResponseDto {

    @Getter
    @Builder
    @FieldDefaults(level = PRIVATE)
    public static class CvTag implements Serializable {

        String id;

        String jobId;

        String createdBy;

        String objectKey;

        String downloadUrl;

        Double score;

        String nextCursor;

        boolean hasNextPage;

    }

}
