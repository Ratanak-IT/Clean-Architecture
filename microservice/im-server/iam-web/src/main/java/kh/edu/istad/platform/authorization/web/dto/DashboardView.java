package kh.edu.istad.platform.authorization.web.dto;

import kh.edu.istad.platform.authorization.domain.dto.user.result.UserResult;

import java.util.Collection;
import java.util.Map;

public record DashboardView(
        UserResult user,
        Collection<String> roles,
        Collection<String> permissions,
        Map<String, Object> claims,
        String username
) {
}

