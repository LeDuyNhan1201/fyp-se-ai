package com.ben.smartcv.job.adapter;

import com.ben.smartcv.common.contract.query.JobQuery;
import com.ben.smartcv.common.util.StringHelper;
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
    public List<ResponseDto.JobDescription> searchJobs(
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
                .education(StringHelper.listToString(education))
                .skills(StringHelper.listToString(skills))
                .experience(StringHelper.listToString(experience))
                .salary(fromSalary == null ? null : Range.closed(fromSalary, toSalary))
                .page(Optional.ofNullable(page).orElse(1))
                .size(Optional.ofNullable(size).orElse(10))
                .build();

        List<ResponseDto.JobDescription> result =
                queryGateway.query(query, ResponseTypes.multipleInstancesOf(ResponseDto.JobDescription.class)).join();

        return result;
    }

}
