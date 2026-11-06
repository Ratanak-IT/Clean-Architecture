package kh.edu.istad.platform.authorization.persistence.adapter;

import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.port.out.UserRepository;
import kh.edu.istad.platform.authorization.persistence.entity.UserEntity;
import kh.edu.istad.platform.authorization.persistence.mapper.UserPersistenceMapper;
import kh.edu.istad.platform.authorization.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
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
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByUuid(UUID uuid) {
        return userJpaRepository.findByUuid(uuid).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userJpaRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).map(mapper::toDomain);
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
    public List<User> findAll() {
        return userJpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public void deleteByUuid(UUID uuid) {
        userJpaRepository.deleteByUuid(uuid);
    }
}
