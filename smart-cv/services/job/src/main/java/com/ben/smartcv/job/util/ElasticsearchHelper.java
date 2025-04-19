package com.ben.smartcv.job.util;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import java.util.List;

public final class ElasticsearchHelper {

    public static void extractedTermFilter(String fieldName, String fieldValue, BoolQuery.Builder b) {
        if (fieldValue == null || fieldValue.isEmpty()) {
            return;
        }
        b.must(m -> m
                .term(t -> t
                        .field(fieldName)
                        .value(fieldValue)
                        .caseInsensitive(true)
                )
        );
    }

    public static void extractedTermsFilter(String fieldName, List<String> fieldValues, BoolQuery.Builder b) {
        if (fieldValues.isEmpty()) {
            return;
        }
        b.must(m -> {
            BoolQuery.Builder innerBool = new BoolQuery.Builder();
            for (String value : fieldValues) {
                innerBool.should(s -> s
                        .term(t -> t
                                .field(fieldName)
                                .value(value)
                                .caseInsensitive(true)
                        )
                );
            }
            return new Query.Builder().bool(innerBool.build());
        });
    }

    public static void extractedRange(String fieldName, Number min, Number max, BoolQuery.Builder bool) {
        if (min != null || max != null) {
            bool.must(m -> m
                    .range(r -> r
                            .term(t -> t
                                    .field(fieldName)
                                    .from(min != null ? min.toString() : null)
                                    .to(max != null ? max.toString() : null)
                            )
                    )
            );
        }
    }

}
