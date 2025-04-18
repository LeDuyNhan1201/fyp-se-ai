package com.ben.smartcv.notification.adapter;

import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.contract.dto.BaseResponse;
import com.ben.smartcv.common.util.Translator;
import com.ben.smartcv.notification.application.dto.RequestDto;
import com.ben.smartcv.notification.application.exception.NotificationError;
import com.ben.smartcv.notification.application.exception.NotificationHttpException;
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
@Tag(name = "Notification Command APIs")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CommandController {

    CommandGateway commandGateway;

    @Operation(summary = "Send Approval Mail", description = "API to Send Approval Mail")
    @PostMapping("/{userId}/user/{jobId}/job")
    @ResponseStatus(OK)
    public ResponseEntity<BaseResponse<?, ?>> sendApprovalMail(
            @PathVariable String userId,
            @PathVariable String jobId,
            @RequestBody @Valid RequestDto.SendApprovalMail request) {
        try {
            String identifier = UUID.randomUUID().toString();
            NotificationCommand.SendApprovalMail command = NotificationCommand.SendApprovalMail.builder()
                    .id(identifier)
                    .title(request.title())
                    .content(request.content())
                    .userId(userId)
                    .jobId(jobId)
                    .build();

            commandGateway.sendAndWait(command,
                    MetaData.with("correlationId", identifier).and("causationId", identifier));

            return ResponseEntity.ok(BaseResponse.builder()
                    .message(Translator.getMessage("SuccessMsg.MailSent"))
                    .build());
        } catch (Exception e) {
            log.error("Error send mail: {}", e.getMessage(), e);
            throw new NotificationHttpException(NotificationError.CAN_NOT_SEND_MAIL, HttpStatus.BAD_REQUEST);
        }
    }

}
