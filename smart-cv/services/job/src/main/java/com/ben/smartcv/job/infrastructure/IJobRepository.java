package com.ben.smartcv.job.infrastructure;

import com.ben.smartcv.job.domain.entity.Job;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IJobRepository extends ElasticsearchRepository<Job, String> {

}
