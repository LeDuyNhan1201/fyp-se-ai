package com.ben.smartcv.job.adapter;

import com.ben.smartcv.common.util.Translator;
import com.ben.smartcv.common.contract.command.JobCommand;
import com.ben.smartcv.common.contract.dto.BaseResponse;
import com.ben.smartcv.common.util.Seeder;
import com.ben.smartcv.job.application.dto.RequestDto;
import com.ben.smartcv.job.application.exception.JobError;
import com.ben.smartcv.job.application.exception.JobHttpException;
import com.ben.smartcv.job.util.ValidationHelper;
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

import java.util.List;
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
        ValidationHelper.validateSalaryRange(request.fromSalary(), request.toSalary());
        try {
            JobCommand.CreateJob command = JobCommand.CreateJob.builder()
                    .id(UUID.randomUUID().toString())
                    .organizationName(request.organizationName())
                    .position(request.position())
                    .requirements(request.requirements())
                    .expiredAt(request.expiredAt().toInstant())
                    .fromSalary(request.fromSalary())
                    .toSalary(request.toSalary())
                    .build();

            commandGateway.sendAndWait(command);

            return ResponseEntity.ok(BaseResponse.builder()
                    .message(Translator.getMessage("SuccessMsg.Created", "New job"))
                    .build());
        } catch (Exception e) {
            log.error("Error creating job: {}", e.getMessage(), e);
            throw new JobHttpException(JobError.CAN_NOT_SAVE_JOB, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/seed")
    @ResponseStatus(OK)
    public ResponseEntity<BaseResponse<?, ?>> seedJobs() {
        try {
            List<String> jobFiles = List.of(
                    "ai_jobs.xlsx", "android_jobs.xlsx", "backend_jobs.xlsx", "devops_jobs.xlsx",
                    "frontend_jobs.xlsx", "ios_jobs.xlsx", "iot_embedded_jobs.xlsx", "tester_jobs.xlsx"
            );

            List<JobCommand.CreateJob> allJobs = jobFiles.stream()
                    .map(Seeder::extractJobDescriptions)
                    .flatMap(List::stream)
                    .toList();

            allJobs.forEach(commandGateway::sendAndWait);

            return ResponseEntity.ok(BaseResponse.builder()
                    .message(Translator.getMessage("SuccessMsg.DataSeeded", String.valueOf(allJobs.size())))
                    .build());
        } catch (Exception e) {
            log.error("Error seeding jobs: ", e);
            throw new JobHttpException(JobError.CAN_NOT_SEED_JOBS, HttpStatus.BAD_REQUEST);
        }
    }

}
