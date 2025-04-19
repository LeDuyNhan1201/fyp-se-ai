package com.ben.smartcv.curriculum_vitae.infrastructure.repository;

import com.ben.smartcv.curriculum_vitae.domain.model.CurriculumVitae;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ICurriculumVitaeRepository extends MongoRepository<CurriculumVitae, String> {

}
