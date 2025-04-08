package com.ben.smartcv.file.adapter;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.contract.dto.BaseResponse;
import com.ben.smartcv.common.util.FileHelper;
import com.ben.smartcv.common.util.Translator;
import com.ben.smartcv.file.application.exception.FileError;
import com.ben.smartcv.file.application.exception.FileHttpException;
import com.ben.smartcv.file.infrastructure.minio.IMinioClient;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/command")
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
    @PostMapping("/upload")
    @ResponseStatus(OK)
    public ResponseEntity<BaseResponse<?,?>> upload(@RequestParam("file") MultipartFile curriculumVitae) {
        String identifier = UUID.randomUUID().toString();
        String cvId = UUID.randomUUID().toString();
        String contentType = curriculumVitae.getContentType();
        assert contentType != null;
        String fileName = FileHelper.generateFileName(contentType.split("/")[0], contentType.split("/")[1]);

        try {
            minioClient.storeObject(FileHelper.convertToFile(curriculumVitae), fileName, contentType, bucketName);
        } catch (IOException e) {
            log.error("Error when parsing file to store: ", e);
            throw new FileHttpException(FileError.CAN_NOT_STORE_FILE, HttpStatus.BAD_REQUEST);
        }

        CvCommand.ApplyCv command = CvCommand.ApplyCv.builder()
                .id(identifier)
                .cvId(cvId)
                .fileMetadataType(contentType.split("/")[1])
                .fileSize(curriculumVitae.getSize())
                .fileName(fileName)
                .build();
        commandGateway.sendAndWait(command, MetaData.with("correlationId", identifier).and("causationId", identifier));
        return ResponseEntity.status(OK).body(
                BaseResponse.builder()
                        .message(Translator.getMessage("SuccessMsg.Created"))
                        .build()
        );
    }

}
