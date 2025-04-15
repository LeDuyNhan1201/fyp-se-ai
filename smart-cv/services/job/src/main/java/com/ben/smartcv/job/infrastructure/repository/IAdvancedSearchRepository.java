package com.ben.smartcv.job.infrastructure.repository;

import com.ben.smartcv.job.domain.model.SlaveJob;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Observed
@Repository
public interface IAdvancedSearchRepository {

    SearchPage<SlaveJob> search(
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
