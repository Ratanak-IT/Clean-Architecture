package kh.edu.istad.platform.authorization.domain.usecase.identity;

import kh.edu.istad.platform.authorization.domain.dto.identity.command.FederatedIdentityCommand;
import kh.edu.istad.platform.authorization.domain.dto.identity.result.IdentityResult;
import kh.edu.istad.platform.authorization.domain.entity.Identity;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.mapper.IdentityDomainMapper;
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
public class LinkIdentityUseCase {

    private final UserRepository userRepository;
    private final IdentityRepository identityRepository;
    private final IdentityDomainMapper identityDomainMapper;

    @Transactional
    public IdentityResult execute(UUID userUuid, FederatedIdentityCommand command) {
        User user = userRepository.findByUuid(userUuid);

        Identity identity = identityDomainMapper.fromLinkCommand(user, command);

        // Business rule in Entity: One identity per provider per user
        List<Identity> existingIdentities = identityRepository.findByUserUuid(userUuid);
        user.validateCanLinkIdentity(existingIdentities, identity);

        Identity saved = identityRepository.save(identity);
        log.info("Linked identity {} to user {}", saved.getProvider(), user.getUsername().value());

        return identityDomainMapper.toResult(saved);
    }
}
