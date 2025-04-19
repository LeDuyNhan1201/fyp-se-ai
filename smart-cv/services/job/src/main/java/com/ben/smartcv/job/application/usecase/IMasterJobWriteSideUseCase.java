package com.ben.smartcv.job.application.usecase;

import com.ben.smartcv.job.domain.model.MasterJob;
import org.springframework.stereotype.Service;

@Service
public interface IMasterJobWriteSideUseCase {

    void create(MasterJob item);

    void update(MasterJob item);

    void delete(String id);

}
