package kh.edu.istad.platform.authorization.persistence.adapter;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.authorization.domain.entity.Role;
import kh.edu.istad.platform.authorization.domain.port.out.role.RoleRepository;
import kh.edu.istad.platform.authorization.persistence.entity.RoleEntity;
import kh.edu.istad.platform.authorization.persistence.mapper.RolePersistenceMapper;
import kh.edu.istad.platform.authorization.persistence.repository.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final RolePersistenceMapper mapper;

    @Override
    public Role save(Role role) {
        RoleEntity entity = mapper.toEntity(role);
        return mapper.toDomain(roleJpaRepository.save(entity));
    }

    @Override
    public Role findByUuid(UUID uuid) {
        return roleJpaRepository.findByUuid(uuid)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Role", uuid));
    }

    @Override
    public Role findByRole(String role) {
        return roleJpaRepository.findByName(role)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Role with name: " + role + " not found"));
    }

    @Override
    public boolean existsByRole(String role) {
        return roleJpaRepository.existsByName(role);
    }

    @Override
    public List<Role> findAll() {
        return roleJpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteByUuid(UUID uuid) {
        roleJpaRepository.findByUuid(uuid).ifPresent(roleJpaRepository::delete);
    }
}
