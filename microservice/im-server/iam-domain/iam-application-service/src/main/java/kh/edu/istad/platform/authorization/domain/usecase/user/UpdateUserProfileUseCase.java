package kh.edu.istad.platform.authorization.domain.usecase.user;

import kh.edu.istad.platform.authorization.domain.dto.user.command.UpdateUserProfileCommand;
import kh.edu.istad.platform.authorization.domain.dto.user.result.UserResult;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.mapper.UserDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.user.UserRepository;
import kh.edu.istad.platform.authorization.domain.valueobject.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Component
public class UpdateUserProfileUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdateUserProfileUseCase.class);

    private final UserRepository userRepository;
    private final UserDomainMapper userDomainMapper;
    private final CurrentActorProvider currentActorProvider;

    public UpdateUserProfileUseCase(
            UserRepository userRepository,
            UserDomainMapper userDomainMapper,
            CurrentActorProvider currentActorProvider
    ) {
        this.userRepository = userRepository;
        this.userDomainMapper = userDomainMapper;
        this.currentActorProvider = currentActorProvider;
    }

    @Transactional
    public UserResult execute(UpdateUserProfileCommand command) {
        UUID userUuid = (command.userUuid() != null)
                ? command.userUuid()
                : currentActorProvider.getCurrentUserUuid();

        User user = userRepository.findByUuid(userUuid);

        if (command.givenName() != null) {
            user.setGivenName(command.givenName());
        }
        if (command.familyName() != null) {
            user.setFamilyName(command.familyName());
        }
        if (command.nickname() != null) {
            user.setNickname(command.nickname());
        }
        if (command.phoneNumber() != null) {
            user.setPhoneNumber(new PhoneNumber(command.phoneNumber()));
        }
        if (command.locale() != null) {
            user.setLocale(command.locale());
        }

        user.setUpdatedAt(Instant.now());
        User updated = userRepository.update(user);
        log.info("Updated profile for user: {}", updated.getUsername().value());
        return userDomainMapper.toResult(updated);
    }
}
