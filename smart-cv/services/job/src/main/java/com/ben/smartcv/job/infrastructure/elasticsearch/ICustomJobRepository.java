package com.ben.smartcv.job.infrastructure.elasticsearch;

import com.ben.smartcv.job.domain.entity.SlaveJob;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICustomJobRepository {

    SearchPage<SlaveJob> findAll(
            String organizationName,
            String position,
            List<String> education,
            List<String> skills,
            List<String> experience,
            Range<Double> salary,
            Pageable pageable
    );

    List<String> autoComplete(String fieldName, String keyword);

}
