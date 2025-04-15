package com.ben.smartcv.curriculum_vitae.application.usecase;

import com.ben.smartcv.curriculum_vitae.domain.model.CurriculumVitae;
import org.springframework.stereotype.Service;

@Service
public interface ICvCommandUseCase {

    void create(CurriculumVitae curriculumVitae);

}
