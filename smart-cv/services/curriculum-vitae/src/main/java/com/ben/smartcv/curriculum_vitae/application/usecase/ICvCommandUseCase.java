package com.ben.smartcv.curriculum_vitae.application.usecase;

import com.ben.smartcv.common.contract.dto.Enum;
import com.ben.smartcv.curriculum_vitae.domain.model.CurriculumVitae;
import org.springframework.stereotype.Service;

@Service
public interface ICvCommandUseCase {

    void create(CurriculumVitae curriculumVitae);

    void updateStatus(String id, Enum.CvStatus status);

}
