package com.ben.smartcv.job.application.projection;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.ben.smartcv.job.domain.entity.Job;
import com.ben.smartcv.job.infrastructure.ElasticSearchHelper;
import com.ben.smartcv.job.infrastructure.ICustomJobRepository;
import com.ben.smartcv.job.infrastructure.impl.CustomJobRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class JobProjection {

    ICustomJobRepository jobRepository;

    public List<Job> getAllJobs(
            String organizationName,
            String position,
            List<String> education,
            List<String> skills,
            List<String> experience,
            int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size);
        SearchPage<Job> searchHits = jobRepository.findAll(
                organizationName, position, education, skills, experience, pageable);

        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

}
