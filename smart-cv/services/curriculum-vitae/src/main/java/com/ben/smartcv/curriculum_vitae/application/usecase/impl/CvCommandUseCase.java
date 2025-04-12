package com.ben.smartcv.curriculum_vitae.application.usecase.impl;

import com.ben.smartcv.common.util.AuthenticationHelper;
import com.ben.smartcv.curriculum_vitae.application.usecase.ICvCommandUseCase;
import com.ben.smartcv.curriculum_vitae.domain.entity.CurriculumVitae;
import com.ben.smartcv.curriculum_vitae.infrastructure.repository.ICurriculumVitaeRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
        curriculumVitae.setCreatedBy(AuthenticationHelper.getUserId());
        curriculumVitae.setUpdatedBy(AuthenticationHelper.getUserId());

        curriculumVitaeRepository.save(curriculumVitae);
    }

}
