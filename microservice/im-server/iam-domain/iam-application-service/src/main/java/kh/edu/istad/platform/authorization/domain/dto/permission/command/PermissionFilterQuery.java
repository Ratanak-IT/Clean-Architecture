package kh.edu.istad.platform.authorization.domain.dto.permission.command;

import lombok.Builder;

import java.util.Locale;
import java.util.Set;

@Builder
public record PermissionFilterQuery(
        String search,
        String resource,
        String action,
        Boolean active,
        Boolean system,
        Integer page,
        Integer size,
        String sortBy,
        String sortDirection
) {
    private static final Set<String> ALLOWED_SORT_PROPERTIES = Set.of(
            "name", "resource", "action", "createdAt", "updatedAt"
    );

    public PermissionFilterQuery {
        search = trimToNull(search);
        resource = normalizePart(resource);
        action = normalizePart(action);
        if (page == null || page < 0) page = 0;
        if (size == null || size <= 0) size = 20;
        if (size > 100) size = 100;
        if (!ALLOWED_SORT_PROPERTIES.contains(sortBy)) sortBy = "createdAt";
        sortDirection = "ASC".equalsIgnoreCase(sortDirection) ? "ASC" : "DESC";
    }

    private static String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static String normalizePart(String value) {
        String trimmed = trimToNull(value);
        return trimmed == null ? null : trimmed.toLowerCase(Locale.ROOT);
    }
}

