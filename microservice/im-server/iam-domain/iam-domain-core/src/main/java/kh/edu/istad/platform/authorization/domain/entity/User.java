package kh.edu.istad.platform.authorization.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.UserId;
import kh.edu.istad.platform.authorization.domain.exception.AuthorizationDomainException;
import kh.edu.istad.platform.authorization.domain.valueobject.Address;
import kh.edu.istad.platform.authorization.domain.valueobject.Email;
import kh.edu.istad.platform.authorization.domain.valueobject.PhoneNumber;
import kh.edu.istad.platform.authorization.domain.valueobject.Username;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class User extends AuditAware<Long> {

    private Long id;
    private UUID uuid;
    private Username username;
    private Email email;
    private boolean emailVerified;
    private String passwordHash;
    private String givenName;
    private String familyName;
    private String middleName;
    private String nickname;
    private String gender;
    private LocalDate birthdate;
    private String profileUrl;
    private String pictureUrl;
    private String websiteUrl;
    private String zoneinfo;
    private String locale;
    private PhoneNumber phoneNumber;
    private boolean phoneNumberVerified;
    private Address address;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private int failedLoginAttempts;
    private Instant lastLoginAt;
    private Instant passwordChangedAt;
    private boolean deleted;
    private Long version;
    private Set<Permission> permissions;
    private Set<Role> roles;

    public User() {
    }

    public User(Long id, UUID uuid, Username username, Email email, boolean emailVerified,
                String passwordHash, String givenName, String familyName, String middleName,
                String nickname, String gender, LocalDate birthdate, String profileUrl,
                String pictureUrl, String websiteUrl, String zoneinfo, String locale,
                PhoneNumber phoneNumber, boolean phoneNumberVerified, Address address,
                boolean enabled, boolean accountNonExpired, boolean accountNonLocked,
                boolean credentialsNonExpired, int failedLoginAttempts, Instant lastLoginAt,
                Instant passwordChangedAt, boolean deleted, Instant deletedAt, String deletedBy,
                Instant createdAt, String createdBy, Instant updatedAt, String updatedBy,
                Long version, Set<Permission> permissions, Set<Role> roles) {
        super(createdAt, createdBy, updatedAt, updatedBy, deletedAt, deletedBy);
        this.id = id;
        this.uuid = uuid;
        this.username = username;
        this.email = email;
        this.emailVerified = emailVerified;
        this.passwordHash = passwordHash;
        this.givenName = givenName;
        this.familyName = familyName;
        this.middleName = middleName;
        this.nickname = nickname;
        this.gender = gender;
        this.birthdate = birthdate;
        this.profileUrl = profileUrl;
        this.pictureUrl = pictureUrl;
        this.websiteUrl = websiteUrl;
        this.zoneinfo = zoneinfo;
        this.locale = locale;
        this.phoneNumber = phoneNumber;
        this.phoneNumberVerified = phoneNumberVerified;
        this.address = address;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.failedLoginAttempts = failedLoginAttempts;
        this.lastLoginAt = lastLoginAt;
        this.passwordChangedAt = passwordChangedAt;
        this.deleted = deleted;
        this.version = version;
        this.permissions = permissions;
        this.roles = roles;
    }


    // Effective permissions = active role permissions + direct GRANT − direct DENY.
    public Set<String> getEffectivePermissions(List<Assignment> directAssignments) {
        Set<String> effective = new HashSet<>();

        // Active role permissions
        if (this.roles != null) {
            for (Role role : this.roles) {
                if (role.isActive() && !role.isDeleted() && role.getPermissions() != null) {
                    for (Permission permission : role.getPermissions()) {
                        if (permission.isActive() && !permission.isDeleted()) {
                            effective.add(permission.getName());
                        }
                    }
                }
            }
        }

        if (directAssignments != null) {
            // Direct GRANT
            for (Assignment assignment : directAssignments) {
                if (assignment.isActive() && assignment.getPermission() != null
                        && assignment.isGrant()) {
                    Permission p = assignment.getPermission();
                    if (p.isActive() && !p.isDeleted()) {
                        effective.add(p.getName());
                    }
                }
            }

            // Direct DENY
            for (Assignment assignment : directAssignments) {
                if (assignment.isActive() && assignment.getPermission() != null
                        && assignment.isDeny()) {
                    effective.remove(assignment.getPermission().getName());
                }
            }
        }

        return effective;
    }

    // Authorities = ROLE_ + role name, and permission name (resource:action).
    public Set<String> getAuthorities(List<Assignment> directAssignments) {
        Set<String> authorities = new HashSet<>();

        // ROLE_ + role name
        if (this.roles != null) {
            for (Role role : this.roles) {
                if (role.isActive() && !role.isDeleted()) {
                    authorities.add("ROLE_" + role.getName());
                }
            }
        }

        // Permission name (resource:action)
        authorities.addAll(getEffectivePermissions(directAssignments));

        return authorities;
    }

    // One identity per provider per user.
    public void validateCanLinkIdentity(List<Identity> existingIdentities, Identity newIdentity) {
        if (newIdentity == null || newIdentity.getProvider() == null) {
            throw new AuthorizationDomainException("Identity provider must not be blank");
        }
        if (existingIdentities != null) {
            boolean alreadyLinked = existingIdentities.stream()
                    .filter(i -> !i.isDeleted() && i.getProvider() != null)
                    .anyMatch(i -> i.getProvider() == newIdentity.getProvider());
            if (alreadyLinked) {
                throw new AuthorizationDomainException("One identity per provider per user allowed. Provider already linked: " + newIdentity.getProvider());
            }
        }
    }

    // A user cannot unlink their last sign-in method.
    public void validateCanUnlinkIdentity(List<Identity> existingIdentities, Identity identityToUnlink) {
        boolean hasPassword = this.passwordHash != null && !this.passwordHash.isBlank();
        long activeIdentities = existingIdentities == null ? 0 :
                existingIdentities.stream().filter(i -> !i.isDeleted()).count();

        if (!hasPassword && activeIdentities <= 1) {
            throw new AuthorizationDomainException("A user cannot unlink their last sign-in method");
        }
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

    public UserId getUserId() {
        return uuid != null ? new UserId(uuid) : null;
    }

    public Username getUsername() {
        return username;
    }

    public String getUsernameValue() {
        return username != null ? username.value() : null;
    }

    public void setUsername(Username username) {
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username != null ? new Username(username) : null;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPassword() {
        return passwordHash;
    }

    public void setPassword(String password) {
        this.passwordHash = password;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFullName() {
        if (givenName == null && familyName == null) return null;
        if (givenName == null) return familyName;
        if (familyName == null) return givenName;
        return givenName + " " + familyName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getZoneinfo() {
        return zoneinfo;
    }

    public void setZoneinfo(String zoneinfo) {
        this.zoneinfo = zoneinfo;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhoneNumberValue() {
        return phoneNumber != null ? phoneNumber.value() : null;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber != null ? new PhoneNumber(phoneNumber) : null;
    }

    public boolean isPhoneNumberVerified() {
        return phoneNumberVerified;
    }

    public void setPhoneNumberVerified(boolean phoneNumberVerified) {
        this.phoneNumberVerified = phoneNumberVerified;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getAddressStreetAddress() {
        return address != null ? address.streetAddress() : null;
    }

    public void setAddressStreetAddress(String addressStreetAddress) {
        this.address = new Address(addressStreetAddress,
                this.address != null ? this.address.locality() : null,
                this.address != null ? this.address.region() : null,
                this.address != null ? this.address.postalCode() : null,
                this.address != null ? this.address.country() : null);
    }

    public String getAddressLocality() {
        return address != null ? address.locality() : null;
    }

    public void setAddressLocality(String addressLocality) {
        this.address = new Address(
                this.address != null ? this.address.streetAddress() : null,
                addressLocality,
                this.address != null ? this.address.region() : null,
                this.address != null ? this.address.postalCode() : null,
                this.address != null ? this.address.country() : null);
    }

    public String getAddressRegion() {
        return address != null ? address.region() : null;
    }

    public void setAddressRegion(String addressRegion) {
        this.address = new Address(
                this.address != null ? this.address.streetAddress() : null,
                this.address != null ? this.address.locality() : null,
                addressRegion,
                this.address != null ? this.address.postalCode() : null,
                this.address != null ? this.address.country() : null);
    }

    public String getAddressPostalCode() {
        return address != null ? address.postalCode() : null;
    }

    public void setAddressPostalCode(String addressPostalCode) {
        this.address = new Address(
                this.address != null ? this.address.streetAddress() : null,
                this.address != null ? this.address.locality() : null,
                this.address != null ? this.address.region() : null,
                addressPostalCode,
                this.address != null ? this.address.country() : null);
    }

    public String getAddressCountry() {
        return address != null ? address.country() : null;
    }

    public void setAddressCountry(String addressCountry) {
        this.address = new Address(
                this.address != null ? this.address.streetAddress() : null,
                this.address != null ? this.address.locality() : null,
                this.address != null ? this.address.region() : null,
                this.address != null ? this.address.postalCode() : null,
                addressCountry);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Instant getPasswordChangedAt() {
        return passwordChangedAt;
    }

    public void setPasswordChangedAt(Instant passwordChangedAt) {
        this.passwordChangedAt = passwordChangedAt;
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

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private UUID uuid;
        private Username username;
        private Email email;
        private boolean emailVerified;
        private String passwordHash;
        private String givenName;
        private String familyName;
        private String middleName;
        private String nickname;
        private String gender;
        private LocalDate birthdate;
        private String profileUrl;
        private String pictureUrl;
        private String websiteUrl;
        private String zoneinfo;
        private String locale;
        private PhoneNumber phoneNumber;
        private boolean phoneNumberVerified;
        private Address address;
        private boolean enabled = true;
        private boolean accountNonExpired = true;
        private boolean accountNonLocked = true;
        private boolean credentialsNonExpired = true;
        private int failedLoginAttempts;
        private Instant lastLoginAt;
        private Instant passwordChangedAt;
        private boolean deleted = false;
        private Instant deletedAt;
        private String deletedBy;
        private Instant createdAt;
        private String createdBy;
        private Instant updatedAt;
        private String updatedBy;
        private Long version;
        private Set<Permission> permissions;
        private Set<Role> roles;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder username(Username username) {
            this.username = username;
            return this;
        }

        public Builder username(String username) {
            this.username = username != null ? new Username(username) : null;
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

        public Builder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public Builder password(String password) {
            this.passwordHash = password;
            return this;
        }

        public Builder givenName(String givenName) {
            this.givenName = givenName;
            return this;
        }

        public Builder familyName(String familyName) {
            this.familyName = familyName;
            return this;
        }

        public Builder middleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder birthdate(LocalDate birthdate) {
            this.birthdate = birthdate;
            return this;
        }

        public Builder profileUrl(String profileUrl) {
            this.profileUrl = profileUrl;
            return this;
        }

        public Builder pictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
            return this;
        }

        public Builder websiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
            return this;
        }

        public Builder zoneinfo(String zoneinfo) {
            this.zoneinfo = zoneinfo;
            return this;
        }

        public Builder locale(String locale) {
            this.locale = locale;
            return this;
        }

        public Builder phoneNumber(PhoneNumber phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber != null ? new PhoneNumber(phoneNumber) : null;
            return this;
        }

        public Builder phoneNumberVerified(boolean phoneNumberVerified) {
            this.phoneNumberVerified = phoneNumberVerified;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public Builder addressStreetAddress(String addressStreetAddress) {
            this.address = new Address(addressStreetAddress,
                    this.address != null ? this.address.locality() : null,
                    this.address != null ? this.address.region() : null,
                    this.address != null ? this.address.postalCode() : null,
                    this.address != null ? this.address.country() : null);
            return this;
        }

        public Builder addressLocality(String addressLocality) {
            this.address = new Address(
                    this.address != null ? this.address.streetAddress() : null,
                    addressLocality,
                    this.address != null ? this.address.region() : null,
                    this.address != null ? this.address.postalCode() : null,
                    this.address != null ? this.address.country() : null);
            return this;
        }

        public Builder addressRegion(String addressRegion) {
            this.address = new Address(
                    this.address != null ? this.address.streetAddress() : null,
                    this.address != null ? this.address.locality() : null,
                    addressRegion,
                    this.address != null ? this.address.postalCode() : null,
                    this.address != null ? this.address.country() : null);
            return this;
        }

        public Builder addressPostalCode(String addressPostalCode) {
            this.address = new Address(
                    this.address != null ? this.address.streetAddress() : null,
                    this.address != null ? this.address.locality() : null,
                    this.address != null ? this.address.region() : null,
                    addressPostalCode,
                    this.address != null ? this.address.country() : null);
            return this;
        }

        public Builder addressCountry(String addressCountry) {
            this.address = new Address(
                    this.address != null ? this.address.streetAddress() : null,
                    this.address != null ? this.address.locality() : null,
                    this.address != null ? this.address.region() : null,
                    this.address != null ? this.address.postalCode() : null,
                    addressCountry);
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder accountNonExpired(boolean accountNonExpired) {
            this.accountNonExpired = accountNonExpired;
            return this;
        }

        public Builder accountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public Builder credentialsNonExpired(boolean credentialsNonExpired) {
            this.credentialsNonExpired = credentialsNonExpired;
            return this;
        }

        public Builder failedLoginAttempts(int failedLoginAttempts) {
            this.failedLoginAttempts = failedLoginAttempts;
            return this;
        }

        public Builder lastLoginAt(Instant lastLoginAt) {
            this.lastLoginAt = lastLoginAt;
            return this;
        }

        public Builder passwordChangedAt(Instant passwordChangedAt) {
            this.passwordChangedAt = passwordChangedAt;
            return this;
        }

        public Builder deleted(boolean deleted) {
            this.deleted = deleted;
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

        public Builder permissions(Set<Permission> permissions) {
            this.permissions = permissions;
            return this;
        }

        public Builder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public User build() {
            return new User(id, uuid, username, email, emailVerified, passwordHash, givenName,
                    familyName, middleName, nickname, gender, birthdate, profileUrl, pictureUrl,
                    websiteUrl, zoneinfo, locale, phoneNumber, phoneNumberVerified, address,
                    enabled, accountNonExpired, accountNonLocked, credentialsNonExpired,
                    failedLoginAttempts, lastLoginAt, passwordChangedAt, deleted, deletedAt,
                    deletedBy, createdAt, createdBy, updatedAt, updatedBy, version, permissions,
                    roles);
        }
    }
}
