package com.ben.smartcv.curriculum_vitae.application.projection;

import com.ben.smartcv.common.contract.query.CvQuery;
import com.ben.smartcv.curriculum_vitae.application.dto.ResponseDto;
import com.ben.smartcv.curriculum_vitae.application.usecase.ICvQueryUseCase;
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
public class CvProjection {

    ICvQueryUseCase useCase;

    @QueryHandler
    public List<ResponseDto.CvTag> handle(CvQuery.Search query) {
        return useCase.search(
                query.getJobId(),
                query.getCreatedBy(),
                query.getCursor(),
                query.getLimit()
        );
    }

}
