package com.ben.smartcv.job.infrastructure;

import com.ben.smartcv.job.domain.entity.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IJobRepository extends MongoRepository<Job, String> {

}
