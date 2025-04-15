package com.ben.smartcv.job.infrastructure.repository.impl;

import com.ben.smartcv.job.domain.model.SlaveJob;
import com.ben.smartcv.job.infrastructure.repository.IAdvancedSearchRepository;
import com.ben.smartcv.job.util.ElasticsearchHelper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Repository
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AdvancedSearchRepositoryImpl implements IAdvancedSearchRepository {

    ElasticsearchOperations elasticsearchOperations;

    @Override
    public SearchPage<SlaveJob> findAll(
            String organizationName,
            String position,
            List<String> educations,
            List<String> skills,
            List<String> experiences,
            Range<Double> salary,
            Pageable pageable) {

        Query query = buildSearchQuery(
                organizationName,
                position,
                educations,
                skills,
                experiences,
                salary,
                pageable
        );

        SearchHits<SlaveJob> searchHits = elasticsearchOperations.search(query, SlaveJob.class);
        SearchPage<SlaveJob> page = SearchHitSupport.searchPageFor(searchHits, query.getPageable());

        log.info("page: {}, size: {}, totalElements: {}, totalPages: {}",
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
        return page;
    }

    @Override
    public List<String> autoComplete(String fieldName, String keyword) {
        NativeQuery matchQuery = NativeQuery.builder()
                .withQuery(q -> q.matchPhrasePrefix(
                        m -> m.field(fieldName).query(keyword)
                ))
                .withSourceFilter(
                        new FetchSourceFilter(
                                new String[] { fieldName },
                                new String[] {  }
                        ))
                .build();

        SearchHits<SlaveJob> searchHits = elasticsearchOperations.search(matchQuery, SlaveJob.class);
        SearchPage<SlaveJob> page = SearchHitSupport.searchPageFor(searchHits, PageRequest.of(1, 20));

        return switch (fieldName) {
            case "organization_name" -> page.stream().map(hit -> hit.getContent().getOrganizationName()).toList();
            case "position" -> page.stream().map(hit -> hit.getContent().getPosition()).toList();
            default -> Collections.emptyList();
        };
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

        if (organizationName != null && !organizationName.isEmpty()) {
            criteria = criteria.and("organizationName").is(organizationName);
        }

        if (position != null && !position.isEmpty()) {
            criteria = criteria.and("position").is(position);
        }

        if (skills != null && !skills.isEmpty()) {
            criteria = criteria.and("skills").in(skills);
        }

        if (education != null && !education.isEmpty()) {
            criteria = criteria.and("educations").in(education);
        }

        if (experience != null && !experience.isEmpty()) {
            criteria = criteria.and("experiences").in(experience);
        }

        if (salary != null) {
            criteria = criteria.and("salary").between(salary.getLowerBound(), salary.getUpperBound());
        }

        return new CriteriaQuery(criteria).setPageable(pageable);
    }

    public SearchPage<SlaveJob> buildSearchQuery1(
            String organizationName,
            String position,
            List<String> educations,
            List<String> skills,
            List<String> experiences,
            Range<Double> salary,
            Pageable pageable
    ) {
        NativeQueryBuilder nativeQuery = NativeQuery.builder()
//                .withAggregation("position", Aggregation.of(a -> a
//                        .terms(ta -> ta.field("position"))))
//                .withAggregation("experiences", Aggregation.of(a -> a
//                        .terms(ta -> ta.field("experiences"))))
//                .withQuery(q -> q
//                        .bool(b -> b
//                                .should(s -> s
//                                        .multiMatch(m -> m
//                                                .fields("position", "experiences")
//                                                .query(position + "|" + StringHelper.listToString(experiences))
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
                        ElasticsearchHelper.extractedTermFilter("organization_name", organizationName, b);
                    }

                    if (position != null) {
                        log.info("position: {}", position);
                        ElasticsearchHelper.extractedTermFilter("position", position, b);
                    }

                    if (skills != null && !skills.isEmpty()) {
                        log.info("skills: {}", skills);
                        ElasticsearchHelper.extractedTermsFilter("educations", educations, b);
                    }

                    if (educations != null && !educations.isEmpty()) {
                        log.info("educations: {}", educations);
                        ElasticsearchHelper.extractedTermsFilter("experiences", experiences, b);
                    }

                    if (experiences != null && !experiences.isEmpty()) {
                        log.info("experiences: {}", experiences);
                        ElasticsearchHelper.extractedTermsFilter("skills", skills, b);
                    }

                    if (salary != null) {
                        log.info("salary: {}", salary);
                        ElasticsearchHelper.extractedRange("salary",
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

        SearchHits<SlaveJob> searchHitsResult = elasticsearchOperations.search(nativeQuery.build(), SlaveJob.class);
        SearchPage<SlaveJob> productPage = SearchHitSupport.searchPageFor(searchHitsResult, nativeQuery.getPageable());
        return productPage;
    }

}
