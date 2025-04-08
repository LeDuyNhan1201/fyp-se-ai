package com.ben.smartcv.job.application.usecase.impl;

import com.ben.smartcv.job.application.usecase.IMasterJobWriteSideUseCase;
import com.ben.smartcv.job.domain.entity.MasterJob;
import com.ben.smartcv.job.infrastructure.repository.IMasterJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional
public class MasterJobWriteSideUseCase implements IMasterJobWriteSideUseCase {

    IMasterJobRepository repository;

    @Override
    public void create(MasterJob item) {
        repository.save(item);
    }

    @Override
    public void update(MasterJob item) {
        log.info("Updating master job: {}", item);
    }

    @Override
    public void delete(String id) {
        log.info("Deleting master job: {}", id);
    }

}
