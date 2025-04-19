package com.ben.smartcv.job.adapter;

import com.ben.smartcv.common.application.exception.CommonError;
import com.ben.smartcv.common.application.exception.CommonHttpException;
import com.ben.smartcv.common.contract.query.JobQuery;
import com.ben.smartcv.job.application.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/query")
@Tag(name = "Job Query APIs")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RestQueryController {

    QueryGateway queryGateway;

    @Operation(summary = "Get Job by id", description = "API to get job details")
    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public ResponseEntity<ResponseDto.JobDescription> getById(@PathVariable String id) {
        try {
            JobQuery.GetById query = JobQuery.GetById.builder().id(id).build();
            ResponseDto.JobDescription response = queryGateway.query(query, ResponseDto.JobDescription.class).join();
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error getting job by id: {}", e.getMessage(), e);
            throw new CommonHttpException(CommonError.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

}
