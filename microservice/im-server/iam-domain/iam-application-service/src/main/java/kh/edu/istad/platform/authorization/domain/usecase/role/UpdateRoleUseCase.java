package kh.edu.istad.platform.authorization.domain.usecase.role;

import kh.edu.istad.common.domain.exception.ResourceAlreadyExistsException;
import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.authorization.domain.dto.role.command.UpdateRoleCommand;
import kh.edu.istad.platform.authorization.domain.dto.role.result.RoleResult;
import kh.edu.istad.platform.authorization.domain.entity.Role;
import kh.edu.istad.platform.authorization.domain.mapper.RoleDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateRoleUseCase {

    private final RoleRepository roleRepository;
    private final RoleDomainMapper roleDomainMapper;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public RoleResult execute(UpdateRoleCommand command) {
        Role role = roleRepository.findByUuid(command.roleUuid());
        if (role == null || role.isDeleted()) {
            throw new ResourceNotFoundException("Role", command.roleUuid());
        }

        role.validateCanModify();

        if (command.name() != null && !command.name().isBlank() && !command.name().equals(role.getName())) {
            if (roleRepository.existsByRole(command.name())) {
                throw new ResourceAlreadyExistsException("Role", "name", command.name());
            }
            role.setName(command.name());
        }

        if (command.description() != null) {
            role.setDescription(command.description());
        }

        String actor = currentActorProvider.getCurrentActor();
        role.setUpdatedAt(Instant.now());
        role.setUpdatedBy(actor);

        Role updated = roleRepository.update(role);
        log.info("Updated role: {} (uuid: {}) by actor: {}", updated.getName(), updated.getUuid(), actor);
        return roleDomainMapper.toResult(updated);
    }
}
