package kh.edu.istad.platform.authorization.domain.usecase.identity;

import kh.edu.istad.platform.authorization.domain.dto.identity.result.IdentityResult;
import kh.edu.istad.platform.authorization.domain.entity.Identity;
import kh.edu.istad.platform.authorization.domain.mapper.IdentityDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.identity.IdentityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ListUserIdentitiesUseCase {

    private final IdentityRepository identityRepository;
    private final IdentityDomainMapper identityDomainMapper;

    @Transactional(readOnly = true)
    public List<IdentityResult> execute(UUID userUuid) {
        List<Identity> identities = identityRepository.findByUserUuid(userUuid);
        return identities.stream()
                .map(identityDomainMapper::toResult)
                .toList();
    }
}
