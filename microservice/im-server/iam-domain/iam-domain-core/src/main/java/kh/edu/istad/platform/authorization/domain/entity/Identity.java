package kh.edu.istad.platform.authorization.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.IdentityId;
import kh.edu.istad.platform.authorization.domain.valueobject.Email;
import kh.edu.istad.platform.authorization.domain.valueobject.IdentityProviderType;

import java.time.Instant;
import java.util.UUID;

public class Identity extends AuditAware<Long> {

    // Internal primary key
    private Long id;
    // Public identifier
    private UUID uuid;
    // Owner -> users.id
    private Long userId;
    // Owner -> users.uuid
    private UUID userUuid;
    // Optional reference to User entity
    private User user;
    // LOCAL, GOOGLE, GITHUB, FACEBOOK, TELEGRAM
    private IdentityProviderType provider;
    // Provider user id; unique with provider
    private String providerSubject;
    // Email returned by the provider
    private Email email;
    // Whether the provider verified the email
    private boolean emailVerified;
    // Name returned by the provider
    private String displayName;
    // Provider handle (GitHub login, Telegram username)
    private String username;
    // Avatar returned by the provider
    private String pictureUrl;
    // Raw provider attributes (JSONB representation)
    private String attributes;
    // When the identity was linked to the user
    private Instant linkedAt;
    // Last sign-in through this provider
    private Instant lastLoginAt;
    // Soft delete
    private boolean deleted;
    // Optimistic lock
    private Long version;
    public Identity() {
    }

    public Identity(Long id, UUID uuid, Long userId, User user, IdentityProviderType provider,
                    String providerSubject, Email email, boolean emailVerified,
                    String displayName, String username, String pictureUrl, String attributes,
                    Instant linkedAt, Instant lastLoginAt, boolean deleted, Instant deletedAt,
                    String deletedBy, Instant createdAt, String createdBy, Instant updatedAt,
                    String updatedBy, Long version) {
        super(createdAt, createdBy, updatedAt, updatedBy, deletedAt, deletedBy);
        this.id = id;
        this.uuid = uuid;
        this.userId = userId;
        this.user = user;
        this.provider = provider;
        this.providerSubject = providerSubject;
        this.email = email;
        this.emailVerified = emailVerified;
        this.displayName = displayName;
        this.username = username;
        this.pictureUrl = pictureUrl;
        this.attributes = attributes;
        this.linkedAt = linkedAt;
        this.lastLoginAt = lastLoginAt;
        this.deleted = deleted;
        this.version = version;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public IdentityId getIdentityId() {
        return uuid != null ? new IdentityId(uuid) : null;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null && user.getId() != null) {
            this.userId = user.getId();
        }
    }

    public IdentityProviderType getProvider() {
        return provider;
    }

    public void setProvider(IdentityProviderType provider) {
        this.provider = provider;
    }

    public void setProvider(String provider) {
        this.provider = provider != null ? IdentityProviderType.fromString(provider) : null;
    }

    public String getProviderSubject() {
        return providerSubject;
    }

    public void setProviderSubject(String providerSubject) {
        this.providerSubject = providerSubject;
    }

    public Email getEmail() {
        return email;
    }

    public String getEmailValue() {
        return email != null ? email.value() : null;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email != null ? new Email(email) : null;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public Instant getLinkedAt() {
        return linkedAt;
    }

    public void setLinkedAt(Instant linkedAt) {
        this.linkedAt = linkedAt;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private UUID uuid;
        private Long userId;
        private UUID userUuid;
        private User user;
        private IdentityProviderType provider;
        private String providerSubject;
        private Email email;
        private boolean emailVerified;
        private String displayName;
        private String username;
        private String pictureUrl;
        private String attributes;
        private Instant linkedAt;
        private Instant lastLoginAt;
        private boolean deleted = false;
        private Instant deletedAt;
        private String deletedBy;
        private Instant createdAt;
        private String createdBy;
        private Instant updatedAt;
        private String updatedBy;
        private Long version;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder userUuid(UUID userUuid) {
            this.userUuid = userUuid;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            if (user != null && user.getId() != null) {
                this.userId = user.getId();
            }
            return this;
        }

        public Builder provider(IdentityProviderType provider) {
            this.provider = provider;
            return this;
        }

        public Builder provider(String provider) {
            this.provider = provider != null ? IdentityProviderType.fromString(provider) : null;
            return this;
        }

        public Builder providerSubject(String providerSubject) {
            this.providerSubject = providerSubject;
            return this;
        }

        public Builder email(Email email) {
            this.email = email;
            return this;
        }

        public Builder email(String email) {
            this.email = email != null ? new Email(email) : null;
            return this;
        }

        public Builder emailVerified(boolean emailVerified) {
            this.emailVerified = emailVerified;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder pictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
            return this;
        }

        public Builder attributes(String attributes) {
            this.attributes = attributes;
            return this;
        }

        public Builder linkedAt(Instant linkedAt) {
            this.linkedAt = linkedAt;
            return this;
        }

        public Builder lastLoginAt(Instant lastLoginAt) {
            this.lastLoginAt = lastLoginAt;
            return this;
        }

        public Builder deleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public Builder isDeleted(boolean isDeleted) {
            this.deleted = isDeleted;
            return this;
        }

        public Builder deletedAt(Instant deletedAt) {
            this.deletedAt = deletedAt;
            return this;
        }

        public Builder deletedBy(String deletedBy) {
            this.deletedBy = deletedBy;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public Builder version(Long version) {
            this.version = version;
            return this;
        }

        public Identity build() {
            Identity identity = new Identity(id, uuid, userId, user, provider, providerSubject,
                    email, emailVerified, displayName, username, pictureUrl,
                    attributes, linkedAt, lastLoginAt, deleted, deletedAt,
                    deletedBy, createdAt, createdBy, updatedAt, updatedBy, version);
            identity.setUserUuid(userUuid);
            return identity;
        }
    }
}
