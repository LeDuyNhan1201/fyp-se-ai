package com.ben.smartcv.job.adapter;

import com.ben.smartcv.common.contract.dto.OffsetPageResponse;
import com.ben.smartcv.common.contract.query.JobQuery;
import com.ben.smartcv.job.application.dto.ResponseDto;
import com.ben.smartcv.job.util.ValidationHelper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.data.domain.Range;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Controller
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GraphQLQueryController {

    QueryGateway queryGateway;

    @QueryMapping
    public OffsetPageResponse<ResponseDto.JobDescription> search(
            @Argument String organizationName,
            @Argument String position,
            @Argument List<String> educations,
            @Argument List<String> skills,
            @Argument List<String> experiences,
            @Argument Double fromSalary,
            @Argument Double toSalary,
            @Argument Integer page,
            @Argument Integer size) {

        ValidationHelper.validateSalaryRangeQuery(fromSalary, toSalary);

        JobQuery.Search query = JobQuery.Search.builder()
                .organizationName(organizationName)
                .position(position)
                .educations(educations)
                .skills(skills)
                .experiences(experiences)
                .salary(fromSalary == null ? null : Range.closed(fromSalary, toSalary))
                .page(Optional.ofNullable(page).orElse(1))
                .size(Optional.ofNullable(size).orElse(10))
                .build();

        List<ResponseDto.JobDescription> result =
                queryGateway.query(query, ResponseTypes.multipleInstancesOf(ResponseDto.JobDescription.class)).join();

        OffsetPageResponse<ResponseDto.JobDescription> offsetPageResponse = OffsetPageResponse.<ResponseDto.JobDescription>builder()
                .items(result)
                .page(!result.isEmpty() ? result.getFirst().getPage() : 1)
                .totalPages(!result.isEmpty() ? result.getFirst().getTotalPages() : 1)
                .build();
        return offsetPageResponse;
    }

}
