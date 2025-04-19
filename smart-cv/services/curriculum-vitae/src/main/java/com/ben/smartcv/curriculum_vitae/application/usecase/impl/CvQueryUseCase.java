package com.ben.smartcv.curriculum_vitae.application.usecase.impl;

import com.ben.smartcv.common.contract.query.CvQuery;
import com.ben.smartcv.curriculum_vitae.application.dto.ResponseDto;
import com.ben.smartcv.curriculum_vitae.application.usecase.ICvQueryUseCase;
import com.ben.smartcv.curriculum_vitae.application.usecase.IDownloadUrlCacheUseCase;
import com.ben.smartcv.curriculum_vitae.domain.model.CurriculumVitae;
import com.ben.smartcv.curriculum_vitae.infrastructure.grpc.GrpcClientFileService;
import com.ben.smartcv.curriculum_vitae.infrastructure.repository.IAdvancedSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Service
public class CvQueryUseCase implements ICvQueryUseCase {

    IAdvancedSearchRepository advancedSearchRepository;

    GrpcClientFileService grpcClientFileService;

    IDownloadUrlCacheUseCase redisUseCase;

    @Override
    public List<ResponseDto.CvTag> search(CvQuery.Search query) {
        int limitPlusOne = query.getLimit() + 1;

        List<CurriculumVitae> cvs = advancedSearchRepository.search(
                query.getJobId(),
                query.getCreatedBy(),
                query.getCursor(),
                limitPlusOne
        );

        boolean hasNextPage = cvs.size() > query.getLimit();
        List<CurriculumVitae> result = hasNextPage ? cvs.subList(0, query.getLimit()) : cvs;
        String nextCursor = hasNextPage ? result.getLast().getId().toHexString() : null;

        List<String> objectKeys = result.stream()
                .map(CurriculumVitae::getObjectKey)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // Check Redis cache for download URLs
        Map<String, String> cachedUrls = redisUseCase.getWithPrefix("cv:download_url:", objectKeys);
        List<String> missingKeys = objectKeys.stream()
                .filter(key -> !cachedUrls.containsKey(key))
                .toList();

        // If there are missing keys, fetch them from the gRPC service
        if (!missingKeys.isEmpty()) {
            Map<String, String> grpcUrls = grpcClientFileService
                    .callGetAllDownloadUrls(missingKeys)
                    .join(); // blocking để lấy kết quả

            grpcUrls.forEach((objectKey, url) -> {
                redisUseCase.setWithTTL("cv:download_url:" + objectKey, url, 1, TimeUnit.DAYS);
                cachedUrls.put(objectKey, url);
            });
        }

        return result.stream()
                .map(cv -> ResponseDto.CvTag.builder()
                        .id(cv.getId().toHexString())
                        .jobId(cv.getJobId())
                        .createdBy(cv.getCreatedBy())
                        .objectKey(cv.getObjectKey())
                        .downloadUrl(cachedUrls.getOrDefault(cv.getObjectKey(),
                                "https://cv.smartcv.com/" + cv.getObjectKey()))
                        .score(cv.getScore())
                        .status(cv.getStatus().getValue())
                        .nextCursor(nextCursor)
                        .hasNextPage(hasNextPage)
                        .build()
                ).toList();
    }

}
