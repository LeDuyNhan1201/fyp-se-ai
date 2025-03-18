package com.ben.smartcv.job.adapter;

import com.ben.smartcv.common.contract.dto.BaseResponse;
import com.ben.smartcv.job.application.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;

@Controller
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class QueryController {

    QueryGateway queryGateway;

    @Operation(summary = "Get all jobs", description = "API to get list of all jobs")
    @GetMapping
    @ResponseStatus(OK)
    public ResponseEntity<BaseResponse<?, ?>> getJobs(@RequestParam(defaultValue = "0") String offset,
                                                     @RequestParam(defaultValue = "100") String limit) {

        return ResponseEntity.status(OK).body(BaseResponse.builder()
                .build());
    }

//    @QueryMapping
//    public List<ResponseDto.JobDescription> searchJobs(
//            @Argument String title,
//            @Argument String description,
//            @Argument String companyName,
//            @Argument String location,
//            @Argument(value = "0") int page,
//            @Argument(value = "10") int size) {
//
//
//    }

}
