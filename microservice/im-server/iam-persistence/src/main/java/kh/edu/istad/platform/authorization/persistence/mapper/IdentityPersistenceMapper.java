package kh.edu.istad.platform.authorization.persistence.mapper;

import kh.edu.istad.platform.authorization.domain.entity.Identity;
import kh.edu.istad.platform.authorization.domain.valueobject.Email;
import kh.edu.istad.platform.authorization.domain.valueobject.IdentityProviderType;
import kh.edu.istad.platform.authorization.persistence.entity.IdentityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IdentityPersistenceMapper {

    default String fromEmail(Email email) {
        return email != null ? email.value() : null;
    }

    default Email toEmail(String email) {
        return email != null ? new Email(email) : null;
    }

    default String fromProvider(IdentityProviderType provider) {
        return provider != null ? provider.name() : null;
    }

    default IdentityProviderType toProvider(String provider) {
        return provider != null ? IdentityProviderType.fromString(provider) : null;
    }

    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "user", ignore = true)
    Identity toDomain(IdentityEntity entity);

    @Mapping(source = "deleted", target = "isDeleted")
    @Mapping(target = "user", ignore = true)
    IdentityEntity toEntity(Identity domain);
}
