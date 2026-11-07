package kh.edu.istad.platform.authorization.domain.port.out.role;

import kh.edu.istad.platform.authorization.domain.entity.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {
    Role save(Role role);
    Optional<Role> findById(Long id);
    Optional<Role> findByUuid(UUID uuid);
    Optional<Role> findByRole(String role);
    boolean existsByRole(String role);
    List<Role> findAll();
    void deleteById(Long id);
    void deleteByUuid(UUID uuid);
}

