package com.ben.smartcv.curriculum_vitae.infrastructure.repository;

import com.ben.smartcv.curriculum_vitae.application.dto.ResponseDto;
import com.ben.smartcv.curriculum_vitae.domain.entity.CurriculumVitae;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Repository
public class CvQueryUseCase {

    CvQueryRepository cvQueryRepository;

    public List<ResponseDto.CvTag> findAllAfter(String lastId, int limit) {
        int limitPlusOne = limit + 1;
        List<CurriculumVitae> cvs = cvQueryRepository.findAllAfter(lastId, limitPlusOne);

        boolean hasNextPage = cvs.size() > limit;
        List<CurriculumVitae> result = hasNextPage ? cvs.subList(0, limit) : cvs;
        String nextCursor = hasNextPage ? result.getLast().getId().toHexString() : null;

        return result.stream().map(
                cv -> ResponseDto.CvTag.builder()
                        .id(cv.getId().toHexString())
                        .objectKey(cv.getObjectKey())
                        .downloadUrl("sdjfhjhsjkfhsjhfdsjkfhskjhfksj")
                        .nextCursor(nextCursor)
                        .hasNextPage(hasNextPage)
                        .build()
        ).toList();
    }

}
