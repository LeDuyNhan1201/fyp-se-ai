package com.ben.smartcv.curriculum_vitae.application.projection;

import com.ben.smartcv.common.contract.query.CvQuery;
import com.ben.smartcv.curriculum_vitae.application.dto.ResponseDto;
import com.ben.smartcv.curriculum_vitae.infrastructure.repository.CvQueryUseCase;
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

    CvQueryUseCase useCase;

    @QueryHandler
    public List<ResponseDto.CvTag> handle(CvQuery.GetAllCvs query) {
        return useCase.findAllAfter(
                query.getCursor(),
                query.getLimit()
        );
    }

}
