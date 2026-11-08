package kh.edu.istad.platform.authorization.domain.dto.permission.command;

import lombok.Builder;

import java.util.Locale;

@Builder
public record CreatePermissionCommand(
        String resource,
        String action,
        String description
) {

    public CreatePermissionCommand {
        resource = normalizePart(resource, "resource");
        action = normalizePart(action, "action");
        description = description == null || description.isBlank() ? null : description.trim();
    }

    public String name() {
        return resource + ":" + action;
    }

    private static String normalizePart(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Permission " + field + " is required");
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
