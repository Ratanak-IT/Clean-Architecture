package kh.edu.istad.platform.authorization.domain.port.out;

import kh.edu.istad.platform.authorization.domain.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUuid(UUID uuid);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String usernameOrEmail);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findAll();
    void deleteById(Long id);
    void deleteByUuid(UUID uuid);
}

