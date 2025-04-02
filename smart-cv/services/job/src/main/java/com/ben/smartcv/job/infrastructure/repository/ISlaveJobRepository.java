package com.ben.smartcv.job.infrastructure.repository;

import com.ben.smartcv.job.domain.entity.SlaveJob;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISlaveJobRepository extends ElasticsearchRepository<SlaveJob, String> {

}
