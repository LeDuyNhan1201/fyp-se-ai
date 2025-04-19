package com.ben.smartcv.job.application.usecase;

import com.ben.smartcv.common.contract.query.JobQuery;
import com.ben.smartcv.job.application.dto.ResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISlaveJobReadSideUseCase {

    List<ResponseDto.JobDescription> search(JobQuery.Search query);

    ResponseDto.JobDescription getById(JobQuery.GetById id);

}
