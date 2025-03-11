package com.ben.smartcv.job.adapter;

import com.ben.smartcv.common.component.Translator;
import com.ben.smartcv.common.contract.command.JobCommand;
import com.ben.smartcv.common.contract.dto.BaseResponse;
import com.ben.smartcv.job.application.dto.RequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping
@Tag(name = "Job Command APIs")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CommandController {

    CommandGateway commandGateway;

    @Operation(summary = "Create JD", description = "API to Create new job description")
    @PostMapping
    @ResponseStatus(OK)
    public BaseResponse<?, ?> createJob(@RequestBody RequestDto.CreateJobDescription requestDto) {
        JobCommand.CreateJob command = JobCommand.CreateJob.builder()
                .id(UUID.randomUUID().toString())
                .organizationName(requestDto.organizationName())
                .jobPosition(requestDto.jobPosition())
                .requirements(requestDto.requirements())
                .build();
        commandGateway.send(command);
        return BaseResponse.builder().message(
                Translator.getMessage("SuccessMsg.Created")).build();
    }

}
