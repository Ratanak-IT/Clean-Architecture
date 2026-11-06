package kh.edu.istad.imserver.init;

import jakarta.annotation.PostConstruct;

import kh.edu.istad.imserver.domain.Permission;
import kh.edu.istad.imserver.domain.Role;
import kh.edu.istad.imserver.domain.User;
import kh.edu.istad.imserver.feature.permission.PermissionJpaRepository;
import kh.edu.istad.imserver.feature.role.RoleJpaRepository;
import kh.edu.istad.imserver.feature.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class InitCore {

    private final PermissionJpaRepository permissionJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init(){
        Permission permission = Permission.builder()
                .permission("user:read:all")
                .description("Read all users")
                .build();

        Role role = Role.builder()
                .role("ADMIN")
                .description("Admin ng hz")
                .build();

        User user = User.builder()
                .familyName("Thai")
                .givenName("Ratanak")
                .email("ratanak@gmail.com")
                .gender("Male")
                .emailVerified(true)
                .username("ratanak")
                .password(passwordEncoder.encode("Ratanak@168"))
                .build();

        if(!userJpaRepository.existsByUsername("ratanak")){
            role = roleJpaRepository.save(role);
            permission = permissionJpaRepository.save(permission);
            user.setRoles(Set.of(role));
            user.setPermissions(Set.of(permission));
            userJpaRepository.save(user);
        }
    }
}
