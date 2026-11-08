package kh.edu.istad.platform.authorization.domain.usecase.user;

import kh.edu.istad.platform.authorization.domain.entity.Role;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.role.RoleRepository;
import kh.edu.istad.platform.authorization.domain.port.out.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RevokeRoleFromUserUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public void execute(UUID userUuid, UUID roleUuid, String reason) {
        User user = userRepository.findByUuid(userUuid);
        Role roleToRevoke = roleRepository.findByUuid(roleUuid);

        if (user.getRoles() != null) {
            Set<Role> updatedRoles = new HashSet<>(user.getRoles());
            boolean removed = updatedRoles.removeIf(r -> r.getUuid().equals(roleUuid));
            if (removed) {
                user.setRoles(updatedRoles);
                userRepository.save(user);
                log.info("Revoked role {} from user {} with reason: {}", roleToRevoke.getName(), user.getUsername().value(), reason);
            }
        }
    }
}
