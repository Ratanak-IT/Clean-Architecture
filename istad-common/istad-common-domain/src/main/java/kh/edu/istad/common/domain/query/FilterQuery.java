package kh.edu.istad.common.domain.query;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public record FilterQuery(Map<String, String> filters, int page, int size) {
    public static final int DEFAULT_SIZE = 10;
    public static final int MAX_SIZE = 100;

    public FilterQuery {
        filters = filters == null
                ? Map.of()
                : Collections.unmodifiableMap(new HashMap<>(filters));
        page = Math.max(page, 0);
        size = size <= 0 ? DEFAULT_SIZE : Math.min(size, MAX_SIZE);
    }

    public static FilterQuery of(Map<String, String> params) {
        Map<String, String> filters = new HashMap<>(params);
        int page = parse(filters.remove("page"), 0);
        int size = parse(filters.remove("size"), DEFAULT_SIZE);
        return new FilterQuery(filters, page, size);
    }

    private static int parse(String value, int defaultValue) {
        if (value == null || value.isBlank()) return defaultValue;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number: " + value);
        }
    }
}