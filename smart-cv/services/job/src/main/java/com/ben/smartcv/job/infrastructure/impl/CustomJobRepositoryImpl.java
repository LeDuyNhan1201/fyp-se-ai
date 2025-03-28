package com.ben.smartcv.job.infrastructure.impl;

import com.ben.smartcv.job.domain.entity.Job;
import com.ben.smartcv.job.infrastructure.ElasticSearchHelper;
import com.ben.smartcv.job.infrastructure.ICustomJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
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

        Query query = buildSearchQuery(
                organizationName,
                position,
                education,
                skills,
                experience,
                salary,
                pageable
        );

        SearchHits<Job> searchHits = elasticsearchOperations.search(query, Job.class);
        SearchPage<Job> productPage = SearchHitSupport.searchPageFor(searchHits, query.getPageable());

        log.info("page: {}, size: {}, totalElements: {}, totalPages: {}",
                productPage.getNumber(), productPage.getSize(), productPage.getTotalElements(), productPage.getTotalPages());
        return productPage;
    }

    private Query buildSearchQuery(
            String organizationName,
            String position,
            List<String> education,
            List<String> skills,
            List<String> experience,
            Range<Double> salary,
            Pageable pageable) {

        Criteria criteria = new Criteria();

        if (organizationName != null) {
            criteria = criteria.and("organizationName").is(organizationName);
        }

        if (position != null) {
            criteria = criteria.and("position").is(position);
        }

        if (skills != null && !skills.isEmpty()) {
            criteria = criteria.and("skills").in(skills);
        }

        if (education != null && !education.isEmpty()) {
            criteria = criteria.and("education").in(education);
        }

        if (experience != null && !experience.isEmpty()) {
            criteria = criteria.and("experience").in(experience);
        }

        if (salary != null) {
            criteria = criteria.and("salary").between(salary.getLowerBound(), salary.getUpperBound());
        }

        return new CriteriaQuery(criteria).setPageable(pageable);
    }


    public SearchHits<Job> buildSearchQuery1(
            String organizationName,
            String position,
            List<String> education,
            List<String> skills,
            List<String> experience,
            Range<Double> salary,
            Pageable pageable
    ) {
        NativeQueryBuilder nativeQuery = NativeQuery.builder()
//                .withAggregation("position", Aggregation.of(a -> a
//                        .terms(ta -> ta.field("position"))))
//                .withAggregation("experience", Aggregation.of(a -> a
//                        .terms(ta -> ta.field("experience"))))
//                .withQuery(q -> q
//                        .bool(b -> b
//                                .should(s -> s
//                                        .multiMatch(m -> m
//                                                .fields("position", "experience")
//                                                .query(position + "|" + StringHelper.listToString(experience))
//                                                .fuzziness(Fuzziness.ONE.asString())
//                                        )
//                                )
//                        )
//                )
                .withPageable(pageable);


        nativeQuery.withFilter(f -> f
                .bool(b -> {
                    if (organizationName != null) {
                        log.info("organizationName: {}", organizationName);
                        ElasticSearchHelper.extractedTermFilter("organization_name", organizationName, b);
                    }

                    if (position != null) {
                        log.info("position: {}", position);
                        ElasticSearchHelper.extractedTermFilter("position", position, b);
                    }

                    if (skills != null && !skills.isEmpty()) {
                        log.info("skills: {}", skills);
                        ElasticSearchHelper.extractedTermsFilter("education", education, b);
                    }

                    if (education != null && !education.isEmpty()) {
                        log.info("education: {}", education);
                        ElasticSearchHelper.extractedTermsFilter("experience", experience, b);
                    }

                    if (experience != null && !experience.isEmpty()) {
                        log.info("experience: {}", experience);
                        ElasticSearchHelper.extractedTermsFilter("skills", skills, b);
                    }

                    if (salary != null) {
                        log.info("salary: {}", salary);
                        ElasticSearchHelper.extractedRange("salary",
                                salary.getLowerBound().getValue().get(), salary.getUpperBound().getValue().get(), b);
                    }

                    b.must(m -> m.term(t -> t.field("is_deleted").value(false)));
                    return b;
                })
        );

//        if (productCriteria.sortType() == SortType.PRICE_ASC) {
//            nativeQuery.withSort(Sort.by(Sort.Direction.ASC, ProductField.PRICE));
//        } else if (productCriteria.sortType() == SortType.PRICE_DESC) {
//            nativeQuery.withSort(Sort.by(Sort.Direction.DESC, ProductField.PRICE));
//        } else {
//            nativeQuery.withSort(Sort.by(Sort.Direction.DESC, ProductField.CREATE_ON));
//        }

        SearchHits<Job> searchHitsResult = elasticsearchOperations.search(nativeQuery.build(), Job.class);
        SearchPage<Job> productPage = SearchHitSupport.searchPageFor(searchHitsResult, nativeQuery.getPageable());

        log.info("page: {}, size: {}, totalElements: {}, totalPages: {}",
                productPage.getNumber(), productPage.getSize(), productPage.getTotalElements(), productPage.getTotalPages());

        List<Job> jobs = searchHitsResult.stream().map(SearchHit::getContent).toList();

        return searchHitsResult;
    }

}
