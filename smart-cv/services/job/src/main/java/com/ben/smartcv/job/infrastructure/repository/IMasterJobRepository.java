package com.ben.smartcv.job.infrastructure.repository;

import com.ben.smartcv.job.domain.entity.MasterJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMasterJobRepository extends JpaRepository<MasterJob, String> {

}
