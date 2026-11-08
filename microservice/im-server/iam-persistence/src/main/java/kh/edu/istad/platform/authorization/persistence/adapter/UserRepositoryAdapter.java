package kh.edu.istad.platform.authorization.persistence.adapter;

import jakarta.persistence.criteria.Predicate;
import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.authorization.domain.dto.PageResult;
import kh.edu.istad.platform.authorization.domain.dto.user.command.UserFilterQuery;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.port.out.user.UserRepository;
import kh.edu.istad.platform.authorization.persistence.entity.UserEntity;
import kh.edu.istad.platform.authorization.persistence.mapper.UserPersistenceMapper;
import kh.edu.istad.platform.authorization.persistence.repository.UserJpaRepository;
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
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper mapper;

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        return mapper.toDomain(userJpaRepository.save(entity));
    }

    @Override
    public User findByUuid(UUID uuid) {
        return userJpaRepository.findByUuid(uuid)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("User", uuid));
    }

    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) {
        return userJpaRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with identifier: " + usernameOrEmail));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public PageResult<User> findAll(UserFilterQuery query) {
        Specification<UserEntity> spec = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isFalse(root.get("isDeleted")));

            if (query.enabled() != null) {
                predicates.add(cb.equal(root.get("enabled"), query.enabled()));
            }

            if (query.emailVerified() != null) {
                predicates.add(cb.equal(root.get("emailVerified"), query.emailVerified()));
            }

            if (query.search() != null && !query.search().isBlank()) {
                String pattern = "%" + query.search().toLowerCase() + "%";
                Predicate usernameMatch = cb.like(cb.lower(root.get("username")), pattern);
                Predicate emailMatch = cb.like(cb.lower(root.get("email")), pattern);
                Predicate givenNameMatch = cb.like(cb.lower(root.get("givenName")), pattern);
                Predicate familyNameMatch = cb.like(cb.lower(root.get("familyName")), pattern);
                predicates.add(cb.or(usernameMatch, emailMatch, givenNameMatch, familyNameMatch));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Sort sort = Sort.by(
                "DESC".equalsIgnoreCase(query.sortDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                query.sortBy()
        );
        Pageable pageable = PageRequest.of(query.page(), query.size(), sort);
        Page<UserEntity> entityPage = userJpaRepository.findAll(spec, pageable);

        List<User> content = entityPage.getContent().stream()
                .map(mapper::toDomain)
                .toList();

        return PageResult.of(content, entityPage.getNumber(), entityPage.getSize(), entityPage.getTotalElements());
    }

    @Override
    public void enable(UUID uuid) {
        UserEntity entity = userJpaRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("User", uuid));
        entity.setEnabled(true);
        entity.setUpdatedAt(Instant.now());
        userJpaRepository.save(entity);
    }

    @Override
    public void disable(UUID uuid) {
        UserEntity entity = userJpaRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("User", uuid));
        entity.setEnabled(false);
        entity.setUpdatedAt(Instant.now());
        userJpaRepository.save(entity);
    }

    @Override
    public void lock(UUID uuid) {
        UserEntity entity = userJpaRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("User", uuid));
        entity.setAccountNonLocked(false);
        entity.setUpdatedAt(Instant.now());
        userJpaRepository.save(entity);
    }

    @Override
    public void unlock(UUID uuid) {
        UserEntity entity = userJpaRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("User", uuid));
        entity.setAccountNonLocked(true);
        entity.setUpdatedAt(Instant.now());
        userJpaRepository.save(entity);
    }

    @Override
    public void deleteByUuid(UUID uuid) {
        UserEntity entity = userJpaRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("User", uuid));
        entity.setDeleted(true);
        entity.setDeletedAt(Instant.now());
        userJpaRepository.save(entity);
    }
}
