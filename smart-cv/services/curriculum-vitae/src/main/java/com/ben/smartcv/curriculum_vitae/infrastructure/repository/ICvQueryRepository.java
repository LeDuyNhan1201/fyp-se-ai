package com.ben.smartcv.curriculum_vitae.infrastructure.repository;

import com.ben.smartcv.curriculum_vitae.domain.entity.CurriculumVitae;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICvQueryRepository {

    List<CurriculumVitae> findAllAfter(String lastId, int limit);

}
