package com.ben.smartcv.job.infrastructure;

import com.ben.smartcv.job.domain.entity.Job;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICustomJobRepository {

    SearchPage<Job> findAll(
            String organizationName,
            String position,
            List<String> education,
            List<String> skills,
            List<String> experience,
            Pageable pageable
    );

}
