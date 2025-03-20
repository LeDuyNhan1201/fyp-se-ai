package com.ben.smartcv.job.infrastructure;

import com.ben.smartcv.job.domain.entity.Job;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICustomJobRepository {

    SearchHits<Job> findAll(
            String organizationName,
            String position,
            List<String> education,
            List<String> skills,
            List<String> experience,
            Range<Double> salary,
            Pageable pageable
    );

}
