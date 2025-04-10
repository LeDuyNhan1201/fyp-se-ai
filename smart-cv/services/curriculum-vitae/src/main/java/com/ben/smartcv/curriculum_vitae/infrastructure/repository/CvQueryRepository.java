package com.ben.smartcv.curriculum_vitae.infrastructure.repository;

import com.ben.smartcv.curriculum_vitae.domain.entity.CurriculumVitae;
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
public class CvQueryRepository {

    MongoTemplate mongoTemplate;

    public List<CurriculumVitae> findAllAfter(String lastId, int limit) {
        Query query = new Query();
        if (lastId != null) {
            query.addCriteria(Criteria.where("_id").lt(new ObjectId(lastId)));
        }
        query.with(Sort.by(Sort.Direction.DESC, "_id"));
        query.limit(limit);
        return mongoTemplate.find(query, CurriculumVitae.class);
    }

}
