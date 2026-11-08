package kh.edu.istad.platform.authorization.domain.dto.role.command;

import lombok.Builder;

@Builder
public record RoleFilterQuery(
        String search,
        Boolean active,
        Boolean system,
        Integer page,
        Integer size,
        String sortBy,
        String sortDirection
) {
    public RoleFilterQuery {
        if (page == null || page < 0) page = 0;
        if (size == null || size <= 0) size = 20;
        if (sortBy == null || sortBy.isBlank()) sortBy = "createdAt";
        if (sortDirection == null || sortDirection.isBlank()) sortDirection = "DESC";
    }
}