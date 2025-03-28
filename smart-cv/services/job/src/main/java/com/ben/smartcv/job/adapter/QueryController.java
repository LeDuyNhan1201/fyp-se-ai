package com.ben.smartcv.job.adapter;

import com.ben.smartcv.common.contract.dto.PageResponse;
import com.ben.smartcv.common.contract.query.JobQuery;
import com.ben.smartcv.job.application.dto.ResponseDto;
import com.ben.smartcv.job.application.exception.JobError;
import com.ben.smartcv.job.application.exception.JobHttpException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.data.domain.Range;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Controller
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class QueryController {

    QueryGateway queryGateway;

    @QueryMapping
    public PageResponse<ResponseDto.JobDescription> searchJobs(
            @Argument String organizationName,
            @Argument String position,
            @Argument List<String> education,
            @Argument List<String> skills,
            @Argument List<String> experience,
            @Argument Double fromSalary,
            @Argument Double toSalary,
            @Argument Integer page,
            @Argument Integer size) {

        if ((fromSalary == null) != (toSalary == null)) {
            throw new JobHttpException(JobError.INVALID_SALARY_RANGE, HttpStatus.BAD_REQUEST);
        }

        if (fromSalary != null && fromSalary > toSalary) {
            throw new JobHttpException(JobError.INVALID_SALARY_RANGE, HttpStatus.BAD_REQUEST);
        }

        JobQuery.GetAllJobs query = JobQuery.GetAllJobs.builder()
                .organizationName(organizationName)
                .position(position)
                .education(education)
                .skills(skills)
                .experience(experience)
                .salary(fromSalary == null ? null : Range.closed(fromSalary, toSalary))
                .page(Optional.ofNullable(page).orElse(1))
                .size(Optional.ofNullable(size).orElse(10))
                .build();

        List<ResponseDto.JobDescription> result =
                queryGateway.query(query, ResponseTypes.multipleInstancesOf(ResponseDto.JobDescription.class)).join();

        PageResponse<ResponseDto.JobDescription> pageResponse = PageResponse.<ResponseDto.JobDescription>builder()
                .items(result)
                .page(!result.isEmpty() ? result.getFirst().getPage() : 1)
                .size(!result.isEmpty() ? result.getFirst().getSize() : 10)
                .totalPages(!result.isEmpty() ? result.getFirst().getTotalPages() : 1)
                .build();
        return pageResponse;
    }

}
