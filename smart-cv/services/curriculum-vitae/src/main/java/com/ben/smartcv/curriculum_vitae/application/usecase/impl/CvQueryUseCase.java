package com.ben.smartcv.curriculum_vitae.application.usecase.impl;

import com.ben.smartcv.common.contract.query.CvQuery;
import com.ben.smartcv.curriculum_vitae.application.dto.ResponseDto;
import com.ben.smartcv.curriculum_vitae.application.usecase.ICvQueryUseCase;
import com.ben.smartcv.curriculum_vitae.domain.model.CurriculumVitae;
import com.ben.smartcv.curriculum_vitae.infrastructure.repository.IAdvancedSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Service
public class CvQueryUseCase implements ICvQueryUseCase {

    IAdvancedSearchRepository advancedSearchRepository;

    @Override
    public List<ResponseDto.CvTag> search(CvQuery.Search query) {
        int limitPlusOne = query.getLimit() + 1;
        List<CurriculumVitae> cvs = advancedSearchRepository.search(
                query.getJobId(),
                query.getCreatedBy(),
                query.getCursor(),
                limitPlusOne);

        boolean hasNextPage = cvs.size() > query.getLimit();
        List<CurriculumVitae> result = hasNextPage ? cvs.subList(0, query.getLimit()) : cvs;
        String nextCursor = hasNextPage ? result.getLast().getId().toHexString() : null;

        return result.stream().map(
                cv -> ResponseDto.CvTag.builder()
                        .id(cv.getId().toHexString())
                        .jobId(cv.getJobId())
                        .createdBy(cv.getCreatedBy())
                        .objectKey(cv.getObjectKey())
                        .downloadUrl("https://cv.smartcv.com/" + cv.getObjectKey())
                        .score(cv.getScore())
                        .nextCursor(nextCursor)
                        .hasNextPage(hasNextPage)
                        .build()
        ).toList();
    }

}
