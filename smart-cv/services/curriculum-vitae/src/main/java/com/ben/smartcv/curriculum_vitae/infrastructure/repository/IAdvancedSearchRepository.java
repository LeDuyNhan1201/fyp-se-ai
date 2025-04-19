package com.ben.smartcv.curriculum_vitae.infrastructure.repository;

import com.ben.smartcv.curriculum_vitae.domain.model.CurriculumVitae;

import java.util.List;

public interface IAdvancedSearchRepository  {

    List<CurriculumVitae> search(
            String jobId,
            String createdBy,
            String lastId,
            int limit);

}
