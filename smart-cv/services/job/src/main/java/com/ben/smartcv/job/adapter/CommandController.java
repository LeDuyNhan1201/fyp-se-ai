package com.ben.smartcv.job.adapter;

import com.ben.smartcv.common.component.Translator;
import com.ben.smartcv.common.contract.command.JobCommand;
import com.ben.smartcv.common.contract.dto.BaseResponse;
import com.ben.smartcv.job.application.dto.RequestDto;
import com.ben.smartcv.job.application.exception.JobError;
import com.ben.smartcv.job.application.exception.JobHttpException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@Tag(name = "Job Command APIs")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CommandController {

    CommandGateway commandGateway;

    @Operation(summary = "Create JD", description = "API to Create new job description")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "common/no-content", description = "No content", content = @Content()),
//            @ApiResponse(responseCode = "", description = ApiConstant.NOT_FOUND,
//                    content = @Content(schema = @Schema(implementation = ErrorVm.class))),
//            @ApiResponse(responseCode = "400", description = "Can not save job",
//                    content = @Content(schema = @Schema(implementation = ErrorVm.class)))})
    @PostMapping
    @ResponseStatus(OK)
    public ResponseEntity<BaseResponse<?, ?>> createJob(@RequestBody @Valid RequestDto.CreateJobDescription request) {
        if (request.fromSalary() == null && request.toSalary() == null) {
            throw new JobHttpException(JobError.INVALID_SALARY_RANGE, HttpStatus.BAD_REQUEST);
        }

        if (request.fromSalary() != null && request.toSalary() != null && request.fromSalary() > request.toSalary()) {
            throw new JobHttpException(JobError.INVALID_SALARY_RANGE, HttpStatus.BAD_REQUEST);
        }
        try {
            String jobId = UUID.randomUUID().toString();
            JobCommand.CreateJob command = JobCommand.CreateJob.builder()
                    .id(UUID.randomUUID().toString())
                    .jobId(jobId)
                    .organizationName(request.organizationName())
                    .position(request.position())
                    .requirements(request.requirements())
                    .expiredAt(request.expiredAt().toInstant())
                    .fromSalary(request.fromSalary())
                    .toSalary(request.toSalary())
                    .build();
            commandGateway.sendAndWait(command);

            return ResponseEntity.status(OK).body(BaseResponse.builder().message(
                    Translator.getMessage("SuccessMsg.Created", "New job")).build());

        } catch (Exception e) {
            log.error("Error: ", e);
            throw new JobHttpException(JobError.CAN_NOT_SAVE_JOB, HttpStatus.BAD_REQUEST);
        }
    }

}
