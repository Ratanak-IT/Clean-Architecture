package kh.edu.istad.platform.authorization.persistence.repository;

import kh.edu.istad.platform.authorization.persistence.entity.UserPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPermissionJpaRepository extends JpaRepository<UserPermissionEntity, Long> {
    List<UserPermissionEntity> findByUserId(Long userId);
    List<UserPermissionEntity> findByPermissionId(Long permissionId);
}
