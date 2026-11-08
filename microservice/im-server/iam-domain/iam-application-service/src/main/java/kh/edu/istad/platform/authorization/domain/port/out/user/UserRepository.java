package kh.edu.istad.platform.authorization.domain.port.out.user;

import kh.edu.istad.platform.authorization.domain.dto.PageResult;
import kh.edu.istad.platform.authorization.domain.dto.user.command.UserFilterQuery;
import kh.edu.istad.platform.authorization.domain.entity.User;

import java.util.UUID;

public interface UserRepository {
    User save(User user);
    User findByUuid(UUID uuid);
    User findByUsernameOrEmail(String usernameOrEmail);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    PageResult<User> findAll(UserFilterQuery query);
    void enable(UUID uuid);
    void disable(UUID uuid);
    void lock(UUID uuid);
    void unlock(UUID uuid);
    void deleteByUuid(UUID uuid);
}
