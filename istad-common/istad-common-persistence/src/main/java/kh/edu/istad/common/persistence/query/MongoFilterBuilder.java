package kh.edu.istad.common.persistence.query;

import kh.edu.istad.common.domain.query.FilterQuery;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

public class MongoFilterBuilder {
    private static final Set<String> DEFAULT_BLOCKED = Set.of("createdBy", "modifiedBy");

    private final ReactiveMongoTemplate mongoTemplate;
    private final Set<String> blockedFields;

    public MongoFilterBuilder(ReactiveMongoTemplate mongoTemplate) {
        this(mongoTemplate, DEFAULT_BLOCKED);
    }

    public MongoFilterBuilder(ReactiveMongoTemplate mongoTemplate, Set<String> blockedFields) {
        this.mongoTemplate = mongoTemplate;
        this.blockedFields = blockedFields;
    }

    public <T> Flux<T> find(Class<T> documentType, FilterQuery filterQuery, Sort sort) {
        return mongoTemplate.find(build(documentType, filterQuery, sort), documentType);
    }

    public Query build(Class<?> documentType, FilterQuery filterQuery, Sort sort) {
        return buildFilter(documentType, filterQuery.filters())
                .with(PageRequest.of(filterQuery.page(), filterQuery.size(), sort));
    }

    public Query buildFilter(Class<?> documentType, Map<String, String> filters) {
        var converter = mongoTemplate.getConverter();
        var entity = converter.getMappingContext().getRequiredPersistentEntity(documentType);
        var conversionService = converter.getConversionService();

        List<Criteria> criteria = new ArrayList<>();

        filters.forEach((key, raw) -> {
            if (raw == null || raw.isBlank()) return;

            int dot = key.lastIndexOf('.');
            Optional<FilterOperator> parsedOp = dot > 0
                    ? FilterOperator.from(key.substring(dot + 1))
                    : Optional.empty();
            String name = parsedOp.isPresent() ? key.substring(0, dot) : key;

            if (blockedFields.contains(name)) return;

            var property = entity.getPersistentProperty(name);
            if (property == null) return;

            boolean valueObject = property.isEntity();
            String field = valueObject ? name + ".value" : name;
            Class<?> type = valueObject ? String.class : property.getType();

            FilterOperator op = parsedOp.orElse(
                    type == String.class ? FilterOperator.LIKE : FilterOperator.EQ);

            criteria.add(toCriteria(field, op, raw.trim(), type, conversionService));
        });

        Query query = new Query();
        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria));
        }
        return query;
    }

    private Criteria toCriteria(String field, FilterOperator op, String value,
                                Class<?> type, ConversionService cs) {
        Criteria c = Criteria.where(field);
        return switch (op) {
            case EQ -> c.is(convert(field, value, type, cs));
            case NE -> c.ne(convert(field, value, type, cs));
            case GT -> c.gt(convert(field, value, type, cs));
            case GTE -> c.gte(convert(field, value, type, cs));
            case LT -> c.lt(convert(field, value, type, cs));
            case LTE -> c.lte(convert(field, value, type, cs));
            case IN -> c.in(Arrays.stream(value.split(","))
                    .map(String::trim)
                    .filter(v -> !v.isEmpty())
                    .map(v -> convert(field, v, type, cs))
                    .toList());
            case LIKE -> c.regex(Pattern.quote(value), "i");
        };
    }

    private Object convert(String field, String value, Class<?> type, ConversionService cs) {
        try {
            if (type == String.class) return value;
            if (type == Instant.class) return Instant.parse(value);
            return cs.convert(value, type);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid value '" + value + "' for filter '" + field + "'");
        }
    }
}