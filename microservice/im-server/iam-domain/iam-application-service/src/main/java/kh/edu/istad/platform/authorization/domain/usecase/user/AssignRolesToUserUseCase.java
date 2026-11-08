package kh.edu.istad.platform.authorization.domain.usecase.user;

import kh.edu.istad.platform.authorization.domain.dto.user.command.AssignRolesToUserCommand;
import kh.edu.istad.platform.authorization.domain.dto.user.result.RoleAssignmentResult;
import kh.edu.istad.platform.authorization.domain.entity.Role;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.mapper.UserDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.role.RoleRepository;
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
public class AssignRolesToUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDomainMapper userDomainMapper;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public List<RoleAssignmentResult> execute(AssignRolesToUserCommand command) {
        User user = userRepository.findByUuid(command.userUuid());

        String assignedBy = (command.assignedBy() != null)
                ? command.assignedBy()
                : currentActorProvider.getCurrentActor();

        Set<Role> roles = user.getRoles() != null ? new HashSet<>(user.getRoles()) : new HashSet<>();
        List<RoleAssignmentResult> results = new ArrayList<>();

        for (UUID roleUuid : command.roleUuids()) {
            Role role = roleRepository.findByUuid(roleUuid);

            // Business rule in Entity: Inactive or deleted role cannot be assigned
            role.validateCanAssign();

            roles.add(role);
            results.add(userDomainMapper.toRoleAssignmentResult(user, role, assignedBy, command.reason()));
        }

        user.setRoles(roles);
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);

        log.info("Assigned {} roles to user {}", results.size(), user.getUsername().value());
        return results;
    }
}
