package com.ben.smartcv.job.infrastructure.impl;

import com.ben.smartcv.job.domain.entity.Job;
import com.ben.smartcv.job.infrastructure.ICustomJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Repository
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CustomJobRepositoryImpl implements ICustomJobRepository {

    ElasticsearchOperations elasticsearchOperations;

    @Override
    public SearchHits<Job> findAll(
            String organizationName,
            String position,
            List<String> education,
            List<String> skills,
            List<String> experience,
            Range<Double> salary,
            Pageable pageable) {

        Query searchQuery = buildSearchQuery(
                organizationName,
                position,
                education,
                skills,
                experience,
                salary,
                pageable
        );

        SearchHits<Job> searchHits = elasticsearchOperations.search(searchQuery, Job.class);
        return searchHits;
    }

    private Query buildSearchQuery(
            String organizationName,
            String position,
            List<String> education,
            List<String> skills,
            List<String> experience,
            Range<Double> salary,
            Pageable pageable) {

        Criteria criteria = new Criteria();

        if (organizationName != null) {
            criteria = criteria.and("organizationName").is(organizationName);
        }

        if (position != null) {
            criteria = criteria.and("position").is(position);
        }

        if (skills != null && !skills.isEmpty()) {
            criteria = criteria.and("skills").in(skills);
        }

        if (education != null && !education.isEmpty()) {
            criteria = criteria.and("education").in(education);
        }

        if (experience != null && !experience.isEmpty()) {
            criteria = criteria.and("experience").in(experience);
        }

        if (salary != null) {
            criteria = criteria.and("salary").between(salary.getLowerBound(), salary.getUpperBound());
        }

        return new CriteriaQuery(criteria).setPageable(pageable);
    }

}
