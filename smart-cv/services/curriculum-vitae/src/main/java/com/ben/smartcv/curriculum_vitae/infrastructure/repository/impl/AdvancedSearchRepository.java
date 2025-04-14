package com.ben.smartcv.curriculum_vitae.infrastructure.repository.impl;

import com.ben.smartcv.curriculum_vitae.domain.entity.CurriculumVitae;
import com.ben.smartcv.curriculum_vitae.infrastructure.repository.IAdvancedSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Repository
public class AdvancedSearchRepository implements IAdvancedSearchRepository {

    MongoTemplate mongoTemplate;

    @Override
    public List<CurriculumVitae> search(String jobId,
                                        String createdBy,
                                        String lastId,
                                        int limit) {
        Query query = new Query();
        if (jobId != null && !jobId.isEmpty()) {
            query.addCriteria(Criteria.where("jobId").is(jobId));
        }
        if (createdBy != null && !createdBy.isEmpty()) {
            query.addCriteria(Criteria.where("createdBy").is(createdBy));
        }
        if (lastId != null) {
            query.addCriteria(Criteria.where("_id").lt(new ObjectId(lastId)));
        }
        query.with(Sort.by(Sort.Direction.DESC, "_id"));
        query.limit(limit);
        return mongoTemplate.find(query, CurriculumVitae.class);
    }

}
