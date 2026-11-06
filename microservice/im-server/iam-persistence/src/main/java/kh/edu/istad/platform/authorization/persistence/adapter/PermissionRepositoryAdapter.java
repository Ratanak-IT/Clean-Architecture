package kh.edu.istad.platform.authorization.persistence.adapter;

import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.port.out.PermissionRepository;
import kh.edu.istad.platform.authorization.persistence.entity.PermissionEntity;
import kh.edu.istad.platform.authorization.persistence.mapper.PermissionPersistenceMapper;
import kh.edu.istad.platform.authorization.persistence.repository.PermissionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PermissionRepositoryAdapter implements PermissionRepository {

    private final PermissionJpaRepository permissionJpaRepository;
    private final PermissionPersistenceMapper mapper;

    @Override
    public Permission save(Permission permission) {
        PermissionEntity entity = mapper.toEntity(permission);
        return mapper.toDomain(permissionJpaRepository.save(entity));
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Permission> findByUuid(UUID uuid) {
        return permissionJpaRepository.findByUuid(uuid).map(mapper::toDomain);
    }

    @Override
    public Optional<Permission> findByPermission(String permission) {
        return permissionJpaRepository.findByName(permission).map(mapper::toDomain);
    }

    @Override
    public boolean existsByPermission(String permission) {
        return permissionJpaRepository.existsByName(permission);
    }

    @Override
    public List<Permission> findAll() {
        return permissionJpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        permissionJpaRepository.deleteById(id);
    }

    @Override
    public void deleteByUuid(UUID uuid) {
        permissionJpaRepository.deleteByUuid(uuid);
    }
}
