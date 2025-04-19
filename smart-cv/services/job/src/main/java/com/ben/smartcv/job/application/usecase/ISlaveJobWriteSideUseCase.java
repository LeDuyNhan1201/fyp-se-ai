package com.ben.smartcv.job.application.usecase;

import com.ben.smartcv.job.domain.model.SlaveJob;
import org.springframework.stereotype.Service;

@Service
public interface ISlaveJobWriteSideUseCase {

    void create(SlaveJob item);

    void update(SlaveJob item);

    void delete(String id);

}
