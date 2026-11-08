package kh.edu.istad.platform.authorization.domain.usecase.role;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.authorization.domain.entity.Role;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeactivateRoleUseCase {

    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public void execute(UUID roleUuid) {
        Role role = roleRepository.findByUuid(roleUuid);
        if (role == null || role.isDeleted()) {
            throw new ResourceNotFoundException("Role", roleUuid);
        }

        role.validateCanModify();

        String actor = currentActorProvider.getCurrentActor();
        role.setActive(false);
        role.setUpdatedAt(Instant.now());
        role.setUpdatedBy(actor);

        roleRepository.update(role);
        log.info("Deactivated role: {} (uuid: {}) by actor: {}", role.getName(), roleUuid, actor);
    }
}
