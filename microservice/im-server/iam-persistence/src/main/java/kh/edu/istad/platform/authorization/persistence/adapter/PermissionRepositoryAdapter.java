package kh.edu.istad.platform.authorization.persistence.adapter;

import jakarta.persistence.criteria.Predicate;
import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.authorization.domain.dto.PageResult;
import kh.edu.istad.platform.authorization.domain.dto.permission.command.PermissionFilterQuery;
import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.port.out.permission.PermissionRepository;
import kh.edu.istad.platform.authorization.persistence.entity.PermissionEntity;
import kh.edu.istad.platform.authorization.persistence.mapper.PermissionPersistenceMapper;
import kh.edu.istad.platform.authorization.persistence.repository.PermissionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
    public Permission findByUuid(UUID uuid) {
        return permissionJpaRepository.findByUuid(uuid)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", uuid));
    }

    @Override
    public boolean existsByName(String name) {
        return permissionJpaRepository.existsByName(name);
    }

    @Override
    public PageResult<Permission> findAll(PermissionFilterQuery query) {
        Specification<PermissionEntity> spec = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isFalse(root.get("isDeleted")));

            if (query.resource() != null && !query.resource().isBlank()) {
                predicates.add(cb.equal(root.get("resource"), query.resource()));
            }

            if (query.action() != null && !query.action().isBlank()) {
                predicates.add(cb.equal(root.get("action"), query.action()));
            }

            if (query.active() != null) {
                predicates.add(cb.equal(root.get("isActive"), query.active()));
            }

            if (query.system() != null) {
                predicates.add(cb.equal(root.get("isSystem"), query.system()));
            }

            if (query.search() != null && !query.search().isBlank()) {
                String pattern = "%" + query.search().toLowerCase() + "%";
                Predicate nameMatch = cb.like(cb.lower(root.get("name")), pattern);
                Predicate descMatch = cb.like(cb.lower(root.get("description")), pattern);
                predicates.add(cb.or(nameMatch, descMatch));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Sort sort = Sort.by(
                "DESC".equalsIgnoreCase(query.sortDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                query.sortBy()
        );
        Pageable pageable = PageRequest.of(query.page(), query.size(), sort);
        Page<PermissionEntity> entityPage = permissionJpaRepository.findAll(spec, pageable);

        List<Permission> content = entityPage.getContent().stream()
                .map(mapper::toDomain)
                .toList();

        return PageResult.of(content, entityPage.getNumber(), entityPage.getSize(), entityPage.getTotalElements());
    }

    @Override
    public Permission update(Permission permission) {
        PermissionEntity entity = permissionJpaRepository.findByUuid(permission.getUuid())
                .orElseThrow(() -> new ResourceNotFoundException("Permission", permission.getUuid()));

        entity.setDescription(permission.getDescription());
        entity.setActive(permission.isActive());
        entity.setDeleted(permission.isDeleted());
        entity.setDeletedAt(permission.getDeletedAt());
        entity.setDeletedBy(permission.getDeletedBy());
        entity.setUpdatedAt(permission.getUpdatedAt() != null ? permission.getUpdatedAt() : Instant.now());
        entity.setUpdatedBy(permission.getUpdatedBy());

        PermissionEntity saved = permissionJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

}
