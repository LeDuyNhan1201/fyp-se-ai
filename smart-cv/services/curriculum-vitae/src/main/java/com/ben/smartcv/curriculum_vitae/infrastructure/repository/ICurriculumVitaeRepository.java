package com.ben.smartcv.curriculum_vitae.infrastructure.repository;

import com.ben.smartcv.curriculum_vitae.domain.entity.CurriculumVitae;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ICurriculumVitaeRepository extends MongoRepository<CurriculumVitae, String> {

}
