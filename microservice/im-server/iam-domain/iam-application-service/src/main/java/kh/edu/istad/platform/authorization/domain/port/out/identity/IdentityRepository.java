package kh.edu.istad.platform.authorization.domain.port.out.identity;

import kh.edu.istad.platform.authorization.domain.entity.Identity;
import kh.edu.istad.platform.authorization.domain.valueobject.IdentityProviderType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IdentityRepository {
    Identity save(Identity identity);
    Identity findById(Long id);
    Identity findByUuid(UUID uuid);
    Optional<Identity> findByProviderAndProviderSubject(IdentityProviderType provider, String providerSubject);
    List<Identity> findByUserId(Long userId);
    List<Identity> findByUserUuid(UUID userUuid);
    boolean existsByProviderAndProviderSubject(IdentityProviderType provider, String providerSubject);
    void deleteById(Long id);
    void deleteByUuid(UUID uuid);
}
