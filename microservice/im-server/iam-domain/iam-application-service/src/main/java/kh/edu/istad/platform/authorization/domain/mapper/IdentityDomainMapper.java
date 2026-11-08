package kh.edu.istad.platform.authorization.domain.mapper;

import kh.edu.istad.platform.authorization.domain.dto.identity.command.FederatedIdentityCommand;
import kh.edu.istad.platform.authorization.domain.dto.identity.command.FederatedUserCommand;
import kh.edu.istad.platform.authorization.domain.dto.identity.result.IdentityResult;
import kh.edu.istad.platform.authorization.domain.entity.Identity;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.valueobject.IdentityProviderType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IdentityDomainMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userUuid", source = "user.uuid")
    @Mapping(target = "provider", source = "providerType")
    @Mapping(target = "providerSubject", source = "command.providerSubject")
    @Mapping(target = "email", expression = "java(command.email() != null && !command.email().isBlank() ? new kh.edu.istad.platform.authorization.domain.valueobject.Email(command.email()) : null)")
    @Mapping(target = "emailVerified", expression = "java(command.email() != null && !command.email().isBlank())")
    @Mapping(target = "displayName", expression = "java(command.name() != null ? command.name() : command.username())")
    @Mapping(target = "username", source = "command.username")
    @Mapping(target = "pictureUrl", source = "command.pictureUrl")
    @Mapping(target = "attributes", source = "command.accessToken")
    @Mapping(target = "linkedAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "lastLoginAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "version", constant = "0L")
    Identity fromFederatedCommand(User user, IdentityProviderType providerType, FederatedUserCommand command);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userUuid", source = "user.uuid")
    @Mapping(target = "provider", expression = "java(kh.edu.istad.platform.authorization.domain.valueobject.IdentityProviderType.fromString(command.provider()))")
    @Mapping(target = "providerSubject", source = "command.providerSubject")
    @Mapping(target = "email", expression = "java(command.email() != null && !command.email().isBlank() ? new kh.edu.istad.platform.authorization.domain.valueobject.Email(command.email()) : null)")
    @Mapping(target = "emailVerified", expression = "java(command.email() != null)")
    @Mapping(target = "displayName", source = "command.name")
    @Mapping(target = "username", source = "command.username")
    @Mapping(target = "pictureUrl", source = "command.pictureUrl")
    @Mapping(target = "attributes", source = "command.accessToken")
    @Mapping(target = "linkedAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "lastLoginAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "version", constant = "0L")
    Identity fromLinkCommand(User user, FederatedIdentityCommand command);

    @Mapping(target = "uuid", source = "identity.uuid")
    @Mapping(target = "provider", expression = "java(identity.getProvider() != null ? identity.getProvider().name() : null)")
    @Mapping(target = "email", expression = "java(identity.getEmail() != null ? identity.getEmail().value() : null)")
    @Mapping(target = "displayName", source = "identity.displayName")
    @Mapping(target = "linkedAt", source = "identity.linkedAt")
    IdentityResult toResult(Identity identity);
}
