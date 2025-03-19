package com.ben.smartcv.job.application.projection;

import com.ben.smartcv.common.contract.query.JobQuery;
import com.ben.smartcv.common.util.StringHelper;
import com.ben.smartcv.job.application.dto.ResponseDto;
import com.ben.smartcv.job.domain.entity.Job;
import com.ben.smartcv.job.infrastructure.ICustomJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class JobProjection {

    ICustomJobRepository jobRepository;

    @QueryHandler
    public ResponseDto.JobDescriptions handle(JobQuery.GetAllJobs query) {
        List<ResponseDto.JobDescription> jobDescriptions = getAllJobs(
                query.getOrganizationName(),
                query.getPosition(),
                Optional.of(StringHelper.stringToArray(query.getEducation())).map(List::of).orElse(null),
                Optional.of(StringHelper.stringToArray(query.getSkills())).map(List::of).orElse(null),
                Optional.of(StringHelper.stringToArray(query.getExperience())).map(List::of).orElse(null),
                query.getSalary(),
                query.getPage(),
                query.getSize()
        ).stream()
                .map(job -> ResponseDto.JobDescription.builder()
                        .id(job.getId())
                        .organizationName(job.getOrganizationName())
                        .position(job.getPosition())
                        .expiredAt(job.getExpiredAt())
                        .salary(job.getSalary())
                        .build())
                .toList();
        
        ResponseDto.JobDescriptions result = ResponseDto.JobDescriptions.builder()
                .jobDescriptions(jobDescriptions)
                .build();
        return result;
    }

    public List<Job> getAllJobs(
            String organizationName,
            String position,
            List<String> education,
            List<String> skills,
            List<String> experience,
            Range<Double> salary,
            int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size);
        SearchPage<Job> searchHits = jobRepository.findAll(
                organizationName, position, education, skills, experience, salary, pageable);

        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

}
