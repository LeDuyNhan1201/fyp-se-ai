package com.ben.smartcv.curriculum_vitae.adapter;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.contract.dto.BaseResponse;
import com.ben.smartcv.common.util.Translator;
import com.ben.smartcv.curriculum_vitae.application.dto.RequestDto;
import com.ben.smartcv.curriculum_vitae.application.exception.CurriculumVitaeError;
import com.ben.smartcv.curriculum_vitae.application.exception.CvHttpException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.MetaData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/command")
@Tag(name = "CV APIs")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CommandController {

    CommandGateway commandGateway;

    @Operation(summary = "Send Approval Mail", description = "API to Send Approval Mail")
    @PostMapping("/approve/{jobId}/job")
    @ResponseStatus(OK)
    public ResponseEntity<BaseResponse<?, ?>> sendApprovalMail(
            @PathVariable String jobId,
            @RequestBody @Valid RequestDto.ApproveCv request) {
        try {
            String identifier = UUID.randomUUID().toString();
            CvCommand.ApproveCv command = CvCommand.ApproveCv.builder()
                    .id(identifier)
                    .title(request.title())
                    .jobId(jobId)
                    .content(request.content())
                    .receiverId(request.receiverId())
                    .build();

            commandGateway.sendAndWait(command,
                    MetaData.with("correlationId", identifier).and("causationId", identifier));

            return ResponseEntity.ok(BaseResponse.builder()
                    .message(Translator.getMessage("SuccessMsg.Updated", "CV"))
                    .build());

        } catch (Exception e) {
            log.error("Error send mail: {}", e.getMessage(), e);
            throw new CvHttpException(CurriculumVitaeError.CAN_NOT_UPDATE_CV, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
