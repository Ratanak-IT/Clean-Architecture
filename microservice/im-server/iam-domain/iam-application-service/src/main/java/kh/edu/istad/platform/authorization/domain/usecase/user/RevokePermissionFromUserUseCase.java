package kh.edu.istad.platform.authorization.domain.usecase.user;

import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.port.out.permission.PermissionRepository;
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
public class RevokePermissionFromUserUseCase {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    @Transactional
    public void execute(UUID userUuid, UUID permissionUuid, String reason) {
        User user = userRepository.findByUuid(userUuid);
        Permission permissionToRevoke = permissionRepository.findByUuid(permissionUuid);

        if (user.getPermissions() != null) {
            Set<Permission> updated = new HashSet<>(user.getPermissions());
            boolean removed = updated.removeIf(p -> p.getUuid().equals(permissionUuid));
            if (removed) {
                user.setPermissions(updated);
                userRepository.save(user);
                log.info("Revoked permission {} from user {} with reason: {}", permissionToRevoke.getName(), user.getUsername().value(), reason);
            }
        }
    }
}
