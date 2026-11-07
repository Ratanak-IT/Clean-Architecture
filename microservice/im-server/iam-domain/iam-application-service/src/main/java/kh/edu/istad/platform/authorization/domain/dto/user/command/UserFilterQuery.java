package kh.edu.istad.platform.authorization.domain.dto.user.command;

import lombok.Builder;

@Builder
public record UserFilterQuery(
        String search,
        Boolean enabled,
        Boolean emailVerified,
        Integer page,
        Integer size,
        String sortBy,
        String sortDirection
) {
    public UserFilterQuery {
        if (page == null || page < 0) page = 0;
        if (size == null || size <= 0) size = 20;
        if (sortBy == null || sortBy.isBlank()) sortBy = "createdAt";
        if (sortDirection == null || sortDirection.isBlank()) sortDirection = "DESC";
    }
}

