package com.ben.smartcv.job.infrastructure.impl;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.ben.smartcv.job.domain.entity.Job;
import com.ben.smartcv.job.infrastructure.ElasticSearchHelper;
import com.ben.smartcv.job.infrastructure.ICustomJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Repository
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CustomJobRepositoryImpl implements ICustomJobRepository {

    ElasticsearchOperations elasticsearchOperations;

    @Override
    public SearchPage<Job> findAll(
            String organizationName,
            String position,
            List<String> education,
            List<String> skills,
            List<String> experience,
            Range<Double> salary,
            Pageable pageable) {

        Query searchQuery = buildSearchQuery(
                organizationName,
                position,
                education,
                skills,
                experience,
                salary
        );

        SearchHits<Job> searchHits = elasticsearchOperations.search(searchQuery, Job.class);
        return SearchHitSupport.searchPageFor(searchHits, pageable);
    }

    private Query buildSearchQuery(
            String organizationName,
            String position,
            List<String> education,
            List<String> skills,
            List<String> experience,
            Range<Double> salary) {

        return NativeQuery.builder()
                .withQuery(q -> q.bool(b -> {
//                    if (organizationName != null) {
//                        b.must(ElasticSearchHelper.matchQuery("organizationName", organizationName));
//                    }
//                    if (position != null) {
//                        b.must(ElasticSearchHelper.matchQuery("position", position));
//                    }
//                    if (education != null && !education.isEmpty()) {
//                        b.filter(ElasticSearchHelper.termsQuery("education.keyword", education));
//                    }
//                    if (skills != null && !skills.isEmpty()) {
//                        b.filter(ElasticSearchHelper.termsQuery("skills.keyword", skills));
//                    }
//                    if (experience != null && !experience.isEmpty()) {
//                        b.filter(ElasticSearchHelper.termsQuery("experience.keyword", experience));
//                    }
//                    if (salary != null) {
//                        b.must(ElasticSearchHelper.rangeQuery("salary", salary));
//                    }
                    return b;
                }))
//                .withAggregation("organizationNameAgg", Aggregation.of(a -> a
//                        .terms(ta -> ta.field("organizationName").size(10))))
//                .withAggregation("positionAgg", Aggregation.of(a -> a
//                        .terms(ta -> ta.field("position").size(10))))
                .build();
    }

}
