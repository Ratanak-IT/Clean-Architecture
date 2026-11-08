package kh.edu.istad.platform.authorization.infra.init;

import jakarta.annotation.PostConstruct;
import kh.edu.istad.platform.authorization.persistence.entity.PermissionEntity;
import kh.edu.istad.platform.authorization.persistence.entity.RoleEntity;
import kh.edu.istad.platform.authorization.persistence.entity.UserEntity;
import kh.edu.istad.platform.authorization.persistence.repository.PermissionJpaRepository;
import kh.edu.istad.platform.authorization.persistence.repository.RoleJpaRepository;
import kh.edu.istad.platform.authorization.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class InitCore {

    private final PermissionJpaRepository permissionJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        PermissionEntity permission = PermissionEntity.builder()
                .uuid(UUID.randomUUID())
                .name("user:read:all")
                .resource("user")
                .action("read")
                .description("Read all users")
                .isActive(true)
                .build();

        RoleEntity role = RoleEntity.builder()
                .uuid(UUID.randomUUID())
                .name("ADMIN")
                .description("Admin role")
                .isActive(true)
                .build();

        UserEntity user = UserEntity.builder()
                .uuid(UUID.randomUUID())
                .familyName("Thai")
                .givenName("Ratanak")
                .email("ratanak@gmail.com")
                .gender("Male")
                .emailVerified(true)
                .username("ratanak")
                .passwordHash(passwordEncoder.encode("Ratanak@168"))
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

        if (!userJpaRepository.existsByUsername("ratanak")) {
            role = roleJpaRepository.save(role);
            permission = permissionJpaRepository.save(permission);

            role.addPermission(permission, "system");
            role = roleJpaRepository.save(role);

            user = userJpaRepository.save(user);
            user.addRole(role, "system");
            user.addPermission(permission, "GRANT", "system");

            userJpaRepository.save(user);
        }
    }
}

