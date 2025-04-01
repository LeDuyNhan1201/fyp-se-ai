package com.ben.smartcv.job.infrastructure.elasticsearch;

import com.ben.smartcv.job.domain.entity.SlaveJob;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IJobRepository extends ElasticsearchRepository<SlaveJob, String> {

}
