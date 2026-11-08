package kh.edu.istad.platform.authorization.domain.usecase.permission;

import kh.edu.istad.common.domain.exception.ResourceAlreadyExistsException;
import kh.edu.istad.platform.authorization.domain.dto.permission.command.CreatePermissionCommand;
import kh.edu.istad.platform.authorization.domain.dto.permission.result.PermissionResult;
import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.mapper.PermissionDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.permission.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreatePermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final PermissionDomainMapper permissionDomainMapper;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public PermissionResult execute(CreatePermissionCommand command) {
        if (permissionRepository.existsByName(command.name())) {
            throw new ResourceAlreadyExistsException("Permission", "name", command.name());
        }

        String actor = currentActorProvider.getCurrentActor();
        Permission permission = permissionDomainMapper.fromCreateCommand(command, actor);
        Permission saved = permissionRepository.save(permission);
        log.info("Created permission: {} (uuid: {}) by actor: {}", saved.getName(), saved.getUuid(), actor);
        return permissionDomainMapper.toResult(saved);
    }
}
