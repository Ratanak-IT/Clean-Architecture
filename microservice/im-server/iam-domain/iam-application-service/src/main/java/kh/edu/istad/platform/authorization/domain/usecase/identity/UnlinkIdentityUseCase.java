package kh.edu.istad.platform.authorization.domain.usecase.identity;

import kh.edu.istad.platform.authorization.domain.entity.Identity;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.exception.AuthorizationDomainException;
import kh.edu.istad.platform.authorization.domain.port.out.identity.IdentityRepository;
import kh.edu.istad.platform.authorization.domain.port.out.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnlinkIdentityUseCase {

    private final UserRepository userRepository;
    private final IdentityRepository identityRepository;

    @Transactional
    public void execute(UUID userUuid, UUID identityUuid) {
        User user = userRepository.findByUuid(userUuid);
        Identity identity = identityRepository.findByUuid(identityUuid);
        if (!userUuid.equals(identity.getUserUuid())){
            throw new AuthorizationDomainException("Identity does not belong to user: " + userUuid);
        }

        List<Identity> existingIdentities = identityRepository.findByUserUuid(userUuid);
        user.validateCanUnlinkIdentity(existingIdentities, identity);

        identityRepository.deleteByUuid(identityUuid);
        log.info("Unlinked identity {} from user {}", identity.getProvider(), userUuid);
    }
}
