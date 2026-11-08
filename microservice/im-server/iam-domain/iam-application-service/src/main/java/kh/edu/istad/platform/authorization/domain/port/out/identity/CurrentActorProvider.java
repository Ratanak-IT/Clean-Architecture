package kh.edu.istad.platform.authorization.domain.port.out.identity;

import java.util.Optional;
import java.util.UUID;

// Implemented by iam-infrastructure reading the SecurityContext.
public interface CurrentActorProvider {
    UUID getCurrentUserUuid();
    Optional<UUID> findCurrentUserUuid();
    String getCurrentUsername();
    String getCurrentActor();
}
