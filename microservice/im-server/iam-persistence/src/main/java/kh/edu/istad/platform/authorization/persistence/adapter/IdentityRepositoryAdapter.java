package kh.edu.istad.platform.authorization.persistence.adapter;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.authorization.domain.entity.Identity;
import kh.edu.istad.platform.authorization.domain.port.out.identity.IdentityRepository;
import kh.edu.istad.platform.authorization.domain.valueobject.IdentityProviderType;
import kh.edu.istad.platform.authorization.persistence.entity.IdentityEntity;
import kh.edu.istad.platform.authorization.persistence.entity.UserEntity;
import kh.edu.istad.platform.authorization.persistence.mapper.IdentityPersistenceMapper;
import kh.edu.istad.platform.authorization.persistence.repository.IdentityJpaRepository;
import kh.edu.istad.platform.authorization.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class IdentityRepositoryAdapter implements IdentityRepository {

    private final IdentityJpaRepository identityJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final IdentityPersistenceMapper mapper;

    @Override
    public Identity save(Identity identity) {
        IdentityEntity entity = mapper.toEntity(identity);
        if (identity.getUserId() != null) {
            UserEntity userRef = userJpaRepository.findById(identity.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", identity.getUserId()));
            entity.setUser(userRef);
        }
        IdentityEntity saved = identityJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Identity findById(Long id) {
        return identityJpaRepository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Identity", id));
    }

    @Override
    public Identity findByUuid(UUID uuid) {
        return identityJpaRepository.findByUuid(uuid)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Identity", uuid));
    }

    @Override
    public Optional<Identity> findByProviderAndProviderSubject(IdentityProviderType provider, String providerSubject) {
        return identityJpaRepository.findByProviderAndProviderSubject(provider.name(), providerSubject)
                .map(mapper::toDomain);
    }

    @Override
    public List<Identity> findByUserId(Long userId) {
        return identityJpaRepository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Identity> findByUserUuid(UUID userUuid) {
        return identityJpaRepository.findByUserUuid(userUuid).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByProviderAndProviderSubject(IdentityProviderType provider, String providerSubject) {
        return identityJpaRepository.existsByProviderAndProviderSubject(provider.name(), providerSubject);
    }

    @Override
    public void deleteById(Long id) {
        identityJpaRepository.deleteById(id);
    }

    @Override
    public void deleteByUuid(UUID uuid) {
        identityJpaRepository.findByUuid(uuid).ifPresent(identityJpaRepository::delete);
    }
}
