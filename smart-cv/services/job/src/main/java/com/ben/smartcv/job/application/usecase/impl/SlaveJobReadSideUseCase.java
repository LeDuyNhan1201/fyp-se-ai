package com.ben.smartcv.job.application.usecase.impl;

import com.ben.smartcv.common.util.TimeHelper;
import com.ben.smartcv.job.application.dto.ResponseDto;
import com.ben.smartcv.job.application.usecase.ISlaveJobReadSideUseCase;
import com.ben.smartcv.job.domain.entity.SlaveJob;
import com.ben.smartcv.job.infrastructure.repository.IAdvancedSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SlaveJobReadSideUseCase implements ISlaveJobReadSideUseCase {

    IAdvancedSearchRepository advancedSearchRepository;

    @Override
    public List<ResponseDto.JobDescription> advancedSearch(
            String organizationName,
            String position,
            List<String> education,
            List<String> skills,
            List<String> experience,
            Range<Double> salary,
            Integer page,
            Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        SearchPage<SlaveJob> searchPage = advancedSearchRepository.findAll(
                organizationName, position, education, skills, experience, salary, pageable);

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
                        .educations(job.getEducations())
                        .experiences(job.getExperiences())
                        .expiredAt(TimeHelper.convertToOffsetDateTime(job.getExpiredAt(), ZoneOffset.UTC))
                        .createdAt(TimeHelper.convertToOffsetDateTime(job.getCreatedAt(), ZoneOffset.UTC))
                        .fromSalary(job.getSalary().getLowerBound().getValue().get()) // Already checked
                        .toSalary(job.getSalary().getUpperBound().getValue().get()) // Already checked

                        .page(searchPage.getNumber() + 1)
                        .totalPages(searchPage.getTotalPages())
                        .build()
                )
                .toList();
        return jobDescriptions;
    }

}
