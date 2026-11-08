package kh.edu.istad.platform.authorization.domain.usecase.identity;

import kh.edu.istad.platform.authorization.domain.entity.Identity;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
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
public class UnlinkMyIdentityUseCase {

    private final UserRepository userRepository;
    private final IdentityRepository identityRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public void execute(UUID identityUuid) {
        UUID currentUuid = currentActorProvider.getCurrentUserUuid();
        User user = userRepository.findByUuid(currentUuid);
        Identity identity = identityRepository.findByUuid(identityUuid);

        List<Identity> existingIdentities = identityRepository.findByUserUuid(currentUuid);
        user.validateCanUnlinkIdentity(existingIdentities, identity);

        identityRepository.deleteByUuid(identityUuid);
        log.info("Unlinked identity {} for current user {}", identity.getProvider(), currentUuid);
    }
}

