package com.ben.smartcv.job.infrastructure;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import org.springframework.data.domain.Range;

import java.util.List;
import java.util.stream.Collectors;

public class ElasticSearchHelper {

    public static List<FieldValue> convertToFieldValues(List<String> values) {
        return values == null ? List.of() : values.stream().map(FieldValue::of).collect(Collectors.toList());
    }

    public static Query matchQuery(String field, String value) {
        return Query.of(q -> q.match(m -> m.field(field).query(value)));
    }

    public static Query termsQuery(String field, List<String> values) {
        return Query.of(q -> q.terms(t -> t.field(field)
                .terms(terms -> terms.value(values.stream()
                .map(FieldValue::of)
                .toList()))));
    }

    public static Query rangeQuery(String field, Range<Double> doubleRange) {
        return Query.of(q -> q.range(r -> r.number(n -> n.field(field)
                .gte(doubleRange.getLowerBound().getValue().orElse(0.0))
                .lte(doubleRange.getLowerBound().getValue().orElse(99999999999999999999.0)))));
    }

}
