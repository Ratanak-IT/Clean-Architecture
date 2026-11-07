package kh.edu.istad.platform.authorization.domain.port.out.permission;

import kh.edu.istad.platform.authorization.domain.entity.Permission;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository {
    Permission save(Permission permission);
    Optional<Permission> findById(Long id);
    Optional<Permission> findByUuid(UUID uuid);
    Optional<Permission> findByPermission(String permission);
    boolean existsByPermission(String permission);
    List<Permission> findAll();
    void deleteById(Long id);
    void deleteByUuid(UUID uuid);
}

