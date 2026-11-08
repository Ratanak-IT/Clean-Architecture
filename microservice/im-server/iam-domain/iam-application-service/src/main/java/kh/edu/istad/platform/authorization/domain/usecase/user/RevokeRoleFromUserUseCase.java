package kh.edu.istad.platform.authorization.domain.usecase.user;

import kh.edu.istad.platform.authorization.domain.dto.user.command.RevokeRoleFromUserCommand;
import kh.edu.istad.platform.authorization.domain.entity.Role;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.role.RoleRepository;
import kh.edu.istad.platform.authorization.domain.port.out.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RevokeRoleFromUserUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public void execute(RevokeRoleFromUserCommand command) {
        Role roleToRevoke = roleRepository.findByUuid(command.roleUuid());
        userRepository.revokeRole(command);
        log.info("Revoked role {} from user {} with reason: {}", roleToRevoke.getName(), command.userUuid(), command.reason());
    }

    @Transactional
    public void execute(UUID userUuid, UUID roleUuid, String reason) {
        String actor = currentActorProvider.getCurrentActor();
        execute(new RevokeRoleFromUserCommand(userUuid, roleUuid, actor, reason));
    }
}
