package kh.edu.istad.platform.authorization.domain.mapper;

import kh.edu.istad.platform.authorization.domain.dto.role.result.RoleResult;
import kh.edu.istad.platform.authorization.domain.dto.role.result.RoleSummary;
import kh.edu.istad.platform.authorization.domain.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleDomainMapper {
    RoleResult toResult(Role role);

    default RoleSummary toSummary(Role role) {
        if (role == null) return null;
        int count = role.getPermissions() != null ? role.getPermissions().size() : 0;
        return RoleSummary.builder()
                .uuid(role.getUuid())
                .name(role.getName())
                .description(role.getDescription())
                .system(role.isSystem())
                .active(role.isActive())
                .permissionCount(count)
                .createdAt(role.getCreatedAt())
                .build();
    }
}
