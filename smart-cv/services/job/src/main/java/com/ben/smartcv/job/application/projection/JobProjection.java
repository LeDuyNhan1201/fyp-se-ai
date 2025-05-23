package com.ben.smartcv.job.application.projection;

import com.ben.smartcv.common.contract.query.JobQuery;
import com.ben.smartcv.job.application.dto.ResponseDto;
import com.ben.smartcv.job.application.usecase.ISlaveJobReadSideUseCase;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class JobProjection {

    ISlaveJobReadSideUseCase useCase;

    @QueryHandler
    public List<ResponseDto.JobDescription> handle(JobQuery.Search query) {
        return useCase.search(query);
    }

    @QueryHandler
    public ResponseDto.JobDescription handle(JobQuery.GetById query) {
        return useCase.getById(query);
    }

}
