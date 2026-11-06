package kh.edu.istad.platform.authorization.persistence.repository;

import kh.edu.istad.platform.authorization.persistence.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionJpaRepository extends JpaRepository<PermissionEntity, Long> {
    Optional<PermissionEntity> findByName(String name);
    Optional<PermissionEntity> findByUuid(UUID uuid);
    boolean existsByName(String name);
    void deleteByUuid(UUID uuid);
}
