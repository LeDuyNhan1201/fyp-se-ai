package com.ben.smartcv.job.application.projection;

import com.ben.smartcv.common.contract.query.JobQuery;
import com.ben.smartcv.common.util.TimeHelper;
import com.ben.smartcv.job.application.dto.ResponseDto;
import com.ben.smartcv.job.domain.entity.Job;
import com.ben.smartcv.job.infrastructure.interfaces.ICustomJobRepository;
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

import java.time.ZoneOffset;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class JobProjection {

    ICustomJobRepository jobRepository;

    @QueryHandler
    public List<ResponseDto.JobDescription> handle(JobQuery.GetAllJobs query) {
        SearchPage<Job> searchPage = getAllJobs(
                query.getOrganizationName(),
                query.getPosition(),
                query.getEducation(),
                query.getSkills(),
                query.getExperience(),
                query.getSalary(),
                query.getPage(),
                query.getSize()
        );
        List<ResponseDto.JobDescription> jobDescriptions = searchPage
                        .stream()
                        .map(SearchHit::getContent)
                        .map(job -> ResponseDto.JobDescription.builder()
                                .id(job.getId())
                                .organizationName(job.getOrganizationName())
                                .email(job.getEmail())
                                .phone(job.getPhone())
                                .position(job.getPosition())
                                .skills(job.getSkills())
                                .education(job.getEducation())
                                .experience(job.getExperience())
                                .expiredAt(TimeHelper.convertToOffsetDateTime(job.getExpiredAt(), ZoneOffset.UTC))
                                .createdAt(TimeHelper.convertToOffsetDateTime(job.getCreatedAt(), ZoneOffset.UTC))
                                .fromSalary(job.getSalary().getLowerBound().getValue().get()) // Already checked
                                .toSalary(job.getSalary().getUpperBound().getValue().get()) // Already checked

                                .page(searchPage.getNumber() + 1)
                                .size(searchPage.getSize())
                                .totalPages(searchPage.getTotalPages())
                                .build()
                        )
                        .toList();
        return jobDescriptions;
    }

    public SearchPage<Job> getAllJobs(
            String organizationName,
            String position,
            List<String> education,
            List<String> skills,
            List<String> experience,
            Range<Double> salary,
            Integer page,
            Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        SearchPage<Job> result = jobRepository.findAll(
                organizationName, position, education, skills, experience, salary, pageable);
        return result;
    }

}
