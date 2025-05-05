package com.ben.smartcv.job.infrastructure.repository;

import com.ben.smartcv.job.domain.model.MasterJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IMasterJobRepository extends JpaRepository<MasterJob, String> {

    Optional<MasterJob> findFirstByOrderByCreatedAtDesc();

}
