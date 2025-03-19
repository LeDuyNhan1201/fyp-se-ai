package com.ben.smartcv.job.adapter;

import com.ben.smartcv.common.contract.query.JobQuery;
import com.ben.smartcv.common.util.StringHelper;
import com.ben.smartcv.job.application.dto.ResponseDto;
import com.ben.smartcv.job.application.exception.JobError;
import com.ben.smartcv.job.application.exception.JobHttpException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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

//    @Operation(summary = "Get all jobs", description = "API to get list of all jobs")
//    @GetMapping
//    @ResponseStatus(OK)
//    public ResponseEntity<BaseResponse<?, ?>> getJobs(@RequestParam(defaultValue = "0") String offset,
//                                                     @RequestParam(defaultValue = "100") String limit) {
//
//        return ResponseEntity.status(OK).body(BaseResponse.builder()
//                .build());
//    }

    @QueryMapping
    public List<ResponseDto.JobDescription> searchJobs(
            @Argument String organizationName,
            @Argument String position,
            @Argument String[] education,
            @Argument String[] skills,
            @Argument String[] experience,
            @Argument Double fromSalary,
            @Argument Double toSalary,
            @Argument Integer page,
            @Argument Integer size) {

        // Kiểm tra nếu chỉ một trong hai giá trị fromSalary hoặc toSalary có giá trị
        if ((fromSalary == null) != (toSalary == null)) {
            throw new JobHttpException(JobError.INVALID_SALARY_RANGE, HttpStatus.BAD_REQUEST);
        }

        // Nếu cả hai không null, kiểm tra điều kiện hợp lệ
        if (fromSalary != null && fromSalary > toSalary) {
            throw new JobHttpException(JobError.INVALID_SALARY_RANGE, HttpStatus.BAD_REQUEST);
        }

        page = (page != null) ? page : 0;
        size = (size != null) ? size : 10;

        // Xây dựng query với điều kiện chỉ khi dữ liệu không null
        JobQuery.GetAllJobs query = JobQuery.GetAllJobs.builder()
                .organizationName(organizationName)
                .position(position)
                .education(StringHelper.arrayToString(education))
                .skills(StringHelper.arrayToString(skills))
                .experience(StringHelper.arrayToString(experience))
                .salary(fromSalary != null ? Range.closed(fromSalary, toSalary) : null) // Chỉ truyền nếu không null
                .page(Optional.of(page).orElse(0))  // ✅ Đảm bảo không null
                .size(Optional.of(size).orElse(10)) // ✅ Đảm bảo không null
                .build();

        ResponseDto.JobDescriptions result = queryGateway.query(query, ResponseDto.JobDescriptions.class).join();

        return result.getJobDescriptions();
    }

}
