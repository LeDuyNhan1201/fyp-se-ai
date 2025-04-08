package com.ben.smartcv.curriculum_vitae.adapter;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.curriculum_vitae.application.exception.CurriculumVitaeError;
import com.ben.smartcv.curriculum_vitae.application.exception.CvHttpException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.MetaData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/cv")
@Tag(name = "CV APIs")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CommandController {

    CommandGateway commandGateway;

//    @Operation(summary = "Upload", description = "API to upload file")
//    @PostMapping
//    @ResponseStatus(OK)
//    public CompletableFuture<String> upload(@RequestPart MultipartFile cv) {
//        CvCommand.ApplyCv command = CvCommand.ApplyCv.builder()
//                .userId(UUID.randomUUID().toString())
//                .cvId(UUID.randomUUID().toString())
//                .build();
//        return commandGateway.send(command, MetaData.with("key", "123"));
//    }

    @Operation(summary = "Upload", description = "API to upload file")
    @GetMapping
    @ResponseStatus(OK)
    public CompletableFuture<String> upload() {
        String identifier = UUID.randomUUID().toString();
        try {
            CvCommand.ProcessCv command = CvCommand.ProcessCv.builder()
                    .id(identifier)
                    .cvId(UUID.randomUUID().toString())
                    .build();
            return commandGateway.send(command, MetaData.with("correlationId", identifier).and("causationId", identifier));
        } catch (Exception e) {
            log.error("Error creating job: {}", e.getMessage(), e);
            throw new CvHttpException(CurriculumVitaeError.CAN_NOT_SAVE_CV, HttpStatus.BAD_REQUEST);
        }

    }

}
