package com.ben.smartcv.curriculum_vitae.application.usecase.impl;

import com.ben.smartcv.common.application.exception.CommonError;
import com.ben.smartcv.common.application.exception.CommonHttpException;
import com.ben.smartcv.common.contract.dto.Enum;
import com.ben.smartcv.curriculum_vitae.application.exception.CvHttpException;
import com.ben.smartcv.curriculum_vitae.application.usecase.ICvCommandUseCase;
import com.ben.smartcv.curriculum_vitae.domain.model.CurriculumVitae;
import com.ben.smartcv.curriculum_vitae.infrastructure.repository.ICurriculumVitaeRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Service
public class CvCommandUseCase implements ICvCommandUseCase {

    ICurriculumVitaeRepository curriculumVitaeRepository;

    @Override
    public void create(CurriculumVitae curriculumVitae) {
        curriculumVitae.setCreatedAt(Instant.now());
        curriculumVitae.setUpdatedAt(Instant.now());
        curriculumVitae.setIsDeleted(false);
        curriculumVitae.setUpdatedBy(curriculumVitae.getCreatedBy());
        curriculumVitae.setStatus(Enum.CvStatus.PENDING);

        curriculumVitaeRepository.save(curriculumVitae);
    }

    @Override
    public void updateStatus(String id, Enum.CvStatus status) {
        CurriculumVitae curriculumVitae = curriculumVitaeRepository.findById(id)
                .orElseThrow(() -> new CommonHttpException(CommonError.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        curriculumVitae.setStatus(status);
        curriculumVitaeRepository.save(curriculumVitae);
    }

}
