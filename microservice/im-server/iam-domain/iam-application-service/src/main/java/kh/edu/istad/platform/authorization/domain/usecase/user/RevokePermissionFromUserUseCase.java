package kh.edu.istad.platform.authorization.domain.usecase.user;

import kh.edu.istad.platform.authorization.domain.dto.user.command.RevokePermissionFromUserCommand;
import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.permission.PermissionRepository;
import kh.edu.istad.platform.authorization.domain.port.out.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RevokePermissionFromUserUseCase {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public void execute(RevokePermissionFromUserCommand command) {
        Permission permissionToRevoke = permissionRepository.findByUuid(command.permissionUuid());
        userRepository.revokePermission(command);
        log.info("Revoked permission {} from user {} with reason: {}", permissionToRevoke.getName(), command.userUuid(), command.reason());
    }

    @Transactional
    public void execute(UUID userUuid, UUID permissionUuid, String reason) {
        String actor = currentActorProvider.getCurrentActor();
        execute(new RevokePermissionFromUserCommand(userUuid, permissionUuid, actor, reason));
    }
}
