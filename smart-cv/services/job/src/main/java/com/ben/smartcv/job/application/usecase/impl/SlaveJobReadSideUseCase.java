package com.ben.smartcv.job.application.usecase.impl;

import com.ben.smartcv.common.application.exception.CommonError;
import com.ben.smartcv.common.application.exception.CommonHttpException;
import com.ben.smartcv.common.contract.query.JobQuery;
import com.ben.smartcv.common.util.TimeHelper;
import com.ben.smartcv.job.application.dto.ResponseDto;
import com.ben.smartcv.job.application.usecase.ISlaveJobReadSideUseCase;
import com.ben.smartcv.job.domain.model.SlaveJob;
import com.ben.smartcv.job.infrastructure.repository.IAdvancedSearchRepository;
import com.ben.smartcv.job.infrastructure.repository.ISlaveJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.http.HttpStatus;
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

    ISlaveJobRepository slaveJobRepository;

    @Override
    public List<ResponseDto.JobDescription> search(JobQuery.Search query) {
        Pageable pageable = PageRequest.of(query.getPage() - 1, query.getSize(),
                Sort.by("createdAt").descending()
                        .and(Sort.by("expiredAt").ascending()));
        SearchPage<SlaveJob> searchPage = advancedSearchRepository.search(
                query.getOrganizationName(),
                query.getPosition(),
                query.getEducations(),
                query.getSkills(),
                query.getExperiences(),
                query.getSalary(),
                pageable);

        List<ResponseDto.JobDescription> jobDescriptions = searchPage
                .stream()
                .map(SearchHit::getContent)
                .map(job -> ResponseDto.JobDescription.builder()
                        .id(job.getId())
                        .createdBy(job.getCreatedBy())
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

    @Override
    public ResponseDto.JobDescription getById(JobQuery.GetById query) {
        SlaveJob job = slaveJobRepository.findById(query.getId()).orElseThrow(
                () -> new CommonHttpException(CommonError.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND)
        );
        return ResponseDto.JobDescription.builder()
                .id(job.getId())
                .createdBy(job.getCreatedBy())
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
                .build();
    }

}
