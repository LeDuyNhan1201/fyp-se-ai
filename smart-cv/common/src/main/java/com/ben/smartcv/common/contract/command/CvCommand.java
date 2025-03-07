package com.ben.smartcv.common.contract.command;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import static lombok.AccessLevel.PRIVATE;

public class CvCommand {

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class ApplyCv extends BaseCommand<String> {

        String cvId;

        String fileName;

        String fileMetadataType;

        String fileSize;

        MultipartFile file;

    }

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class ProcessCv extends BaseCommand<String> {

        String cvId;

        String objectKey;

    }

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class RollbackProcessCv extends BaseCommand<String> {

        String cvId;

        String objectKey;

    }

}
