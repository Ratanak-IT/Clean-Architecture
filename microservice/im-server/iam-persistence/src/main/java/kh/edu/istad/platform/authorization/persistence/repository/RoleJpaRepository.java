package kh.edu.istad.platform.authorization.persistence.repository;

import kh.edu.istad.platform.authorization.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);
    Optional<RoleEntity> findByUuid(UUID uuid);
    boolean existsByName(String name);
    void deleteByUuid(UUID uuid);
}
