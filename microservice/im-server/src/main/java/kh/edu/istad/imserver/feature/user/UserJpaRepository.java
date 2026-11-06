package kh.edu.istad.imserver.feature.user;

import kh.edu.istad.imserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);
}
