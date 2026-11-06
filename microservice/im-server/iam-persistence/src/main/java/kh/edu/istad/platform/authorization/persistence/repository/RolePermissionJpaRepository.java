package kh.edu.istad.platform.authorization.persistence.repository;

import kh.edu.istad.platform.authorization.persistence.entity.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionJpaRepository extends JpaRepository<RolePermissionEntity, Long> {
    List<RolePermissionEntity> findByRoleId(Long roleId);
    List<RolePermissionEntity> findByPermissionId(Long permissionId);
}
