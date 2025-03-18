package com.ben.smartcv.job.infrastructure;

import com.ben.smartcv.job.domain.entity.Job;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IJobRepository extends ElasticsearchRepository<Job, String> {

}
