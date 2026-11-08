package kh.edu.istad.platform.authorization.domain.usecase.user;

import kh.edu.istad.platform.authorization.domain.dto.user.command.CreateUserCommand;
import kh.edu.istad.platform.authorization.domain.dto.user.result.UserResult;
import kh.edu.istad.platform.authorization.domain.entity.Role;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.mapper.UserDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.identity.PasswordHasher;
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
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordHasher passwordHasher;
    private final UserDomainMapper userDomainMapper;

    @Transactional
    public UserResult execute(CreateUserCommand command) {
        if (userRepository.existsByUsername(command.username())) {
            throw new IllegalArgumentException("Username is already in use: " + command.username());
        }

        if (userRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Email is already in use: " + command.email());
        }

        String hashedPassword = command.password() != null
                ? passwordHasher.hash(command.password())
                : null;

        Set<Role> roles = new HashSet<>();
        if (command.roleUuids() != null) {
            for (UUID roleUuid : command.roleUuids()) {
                roles.add(roleRepository.findByUuid(roleUuid));
            }
        }

        User user = userDomainMapper.fromCreateCommand(command, hashedPassword, roles);

        User saved = userRepository.save(user);
        log.info("Created user: {}", saved.getUsername().value());
        return userDomainMapper.toResult(saved);
    }
}
