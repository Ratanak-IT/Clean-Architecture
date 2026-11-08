package kh.edu.istad.platform.authorization.domain.port.out.permission;

import kh.edu.istad.platform.authorization.domain.dto.PageResult;
import kh.edu.istad.platform.authorization.domain.dto.permission.command.PermissionFilterQuery;
import kh.edu.istad.platform.authorization.domain.entity.Permission;

import java.util.UUID;

public interface PermissionRepository {
    Permission save(Permission permission);
    Permission findByUuid(UUID uuid);
    boolean existsByName(String name);
    PageResult<Permission> findAll(PermissionFilterQuery query);
    Permission update(Permission permission);
}