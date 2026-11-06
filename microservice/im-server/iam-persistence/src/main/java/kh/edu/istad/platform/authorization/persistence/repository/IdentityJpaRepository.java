package kh.edu.istad.platform.authorization.persistence.repository;

import kh.edu.istad.platform.authorization.persistence.entity.IdentityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IdentityJpaRepository extends JpaRepository<IdentityEntity, Long> {
    Optional<IdentityEntity> findByUuid(UUID uuid);
    Optional<IdentityEntity> findByProviderAndProviderSubject(String provider, String providerSubject);
    List<IdentityEntity> findByUserUuid(UUID userUuid);
    List<IdentityEntity> findByUserId(Long userId);
    boolean existsByProviderAndProviderSubject(String provider, String providerSubject);
}
