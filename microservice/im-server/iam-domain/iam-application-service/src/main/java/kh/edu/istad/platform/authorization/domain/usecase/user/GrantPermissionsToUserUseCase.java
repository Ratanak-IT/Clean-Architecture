package kh.edu.istad.platform.authorization.domain.usecase.user;

import kh.edu.istad.platform.authorization.domain.dto.user.command.GrantPermissionsToUserCommand;
import kh.edu.istad.platform.authorization.domain.dto.user.result.PermissionGrantResult;
import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.mapper.UserDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.permission.PermissionRepository;
import kh.edu.istad.platform.authorization.domain.port.out.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GrantPermissionsToUserUseCase {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final UserDomainMapper userDomainMapper;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public List<PermissionGrantResult> execute(GrantPermissionsToUserCommand command) {
        User user = userRepository.findByUuid(command.userUuid());

        String grantedBy = (command.grantedBy() != null)
                ? command.grantedBy()
                : currentActorProvider.getCurrentActor();

        Set<Permission> permissions = user.getPermissions() != null ? new HashSet<>(user.getPermissions()) : new HashSet<>();
        List<PermissionGrantResult> results = new ArrayList<>();

        for (UUID permUuid : command.permissionUuids()) {
            Permission permission = permissionRepository.findByUuid(permUuid);

            // Business rule in Entity: Inactive or deleted permission cannot be assigned
            permission.validateCanAssign();

            permissions.add(permission);
            results.add(userDomainMapper.toPermissionGrantResult(user, permission, grantedBy, command.reason()));
        }

        user.setPermissions(permissions);
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);

        log.info("Granted {} permissions to user {}", results.size(), user.getUsername().value());
        return results;
    }
}
