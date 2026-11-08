package kh.edu.istad.platform.authorization.domain.usecase.identity;

import kh.edu.istad.platform.authorization.domain.dto.identity.result.IdentityResult;
import kh.edu.istad.platform.authorization.domain.entity.Identity;
import kh.edu.istad.platform.authorization.domain.mapper.IdentityDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.identity.IdentityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetMyIdentitiesUseCase {

    private final IdentityRepository identityRepository;
    private final IdentityDomainMapper identityDomainMapper;
    private final CurrentActorProvider currentActorProvider;

    @Transactional(readOnly = true)
    public List<IdentityResult> execute() {
        UUID currentUuid = currentActorProvider.getCurrentUserUuid();
        List<Identity> identities = identityRepository.findByUserUuid(currentUuid);
        return identities.stream()
                .map(identityDomainMapper::toResult)
                .toList();
    }
}

