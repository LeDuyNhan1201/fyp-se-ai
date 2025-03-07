package com.ben.smartcv.file.adapter;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.util.FileHelper;
import com.ben.smartcv.file.application.dto.RequestDto;
import com.ben.smartcv.file.application.exception.FileErrorCode;
import com.ben.smartcv.file.application.exception.FileException;
import com.ben.smartcv.file.infrastructure.IMinioClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.MetaData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/file")
@Tag(name = "File APIs")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CommandController {

    CommandGateway commandGateway;

    IMinioClient minioClient;

    @Value("${minio.bucket-name}")
    @NonFinal
    String bucketName;

    @Operation(summary = "Upload", description = "API to upload file")
    @PostMapping
    @ResponseStatus(OK)
    public CompletableFuture<String> upload(@RequestPart MultipartFile curriculumVitae) {
        String cvId = UUID.randomUUID().toString();
        String contentType = curriculumVitae.getContentType();
        assert contentType != null;
        String fileName = FileHelper.generateFileName(contentType.split("/")[0], contentType.split("/")[1]);

        try {
            minioClient.storeObject(FileHelper.convertToFile(curriculumVitae), fileName, contentType, bucketName);
        } catch (IOException e) {
            log.error("Error when parsing file to store: ", e);
            throw new FileException(FileErrorCode.CAN_NOT_STORE_FILE, HttpStatus.BAD_REQUEST);
        }

        CvCommand.ApplyCv command = CvCommand.ApplyCv.builder()
                .cvId(cvId)
                .fileMetadataType(contentType.split("/")[1])
                .fileName(fileName)
                .build();
        return commandGateway.send(command, MetaData.with("key", "123"));
    }

//    @Operation(summary = "Upload", description = "API to upload file")
//    @PostMapping
//    @ResponseStatus(OK)
//    public CompletableFuture<String> upload(@RequestBody RequestDto.CreateCv requestDto) {
//        CvCommand.ApplyCv command = CvCommand.ApplyCv.builder()
//                .id(UUID.randomUUID().toString())
//                .cvId(UUID.randomUUID().toString())
//                .build();
//        return commandGateway.send(command, MetaData.with("key", "123"));
//    }

}
