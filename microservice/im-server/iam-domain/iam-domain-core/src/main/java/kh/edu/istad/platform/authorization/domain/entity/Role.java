package kh.edu.istad.platform.authorization.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.RoleId;
import kh.edu.istad.platform.authorization.domain.exception.AuthorizationDomainException;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Role extends AuditAware<Long> {

    // Internal primary key
    private Long id;
    // Public identifier
    private UUID uuid;
    // Role name, e.g. ADMIN
    private String name;
    // Human-readable purpose of the role
    private String description;
    // Built-in, protected
    private boolean system;
    // Active flag
    private boolean active;
    // Soft delete
    private boolean deleted;
    // Optimistic lock
    private Long version;
    // Associated permissions & users
    private Set<Permission> permissions;
    private List<User> users;
    public Role() {
    }
    public Role(Long id, UUID uuid, String name, String description, boolean system,
                boolean active, boolean deleted, Instant deletedAt, String deletedBy,
                Instant createdAt, String createdBy, Instant updatedAt, String updatedBy,
                Long version, Set<Permission> permissions, List<User> users) {
        super(createdAt, createdBy, updatedAt, updatedBy, deletedAt, deletedBy);
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.system = system;
        this.active = active;
        this.deleted = deleted;
        this.version = version;
        this.permissions = permissions;
        this.users = users;
    }


    // System roles cannot be changed or deleted.
    public void validateCanModify() {
        if (this.system) {
            throw new AuthorizationDomainException("System role cannot be changed or deleted: " + this.name);
        }
    }

    // Inactive roles cannot be assigned.
    public void validateCanAssign() {
        if (!this.active || this.deleted) {
            throw new AuthorizationDomainException("Inactive or deleted role cannot be assigned: " + this.name);
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

    public RoleId getRoleId() {
        return uuid != null ? new RoleId(uuid) : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Alias for role name compatibility
    public String getRole() {
        return name;
    }

    public void setRole(String role) {
        this.name = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private UUID uuid;
        private String name;
        private String description;
        private boolean system = false;
        private boolean active = true;
        private boolean deleted = false;
        private Instant deletedAt;
        private String deletedBy;
        private Instant createdAt;
        private String createdBy;
        private Instant updatedAt;
        private String updatedBy;
        private Long version;
        private Set<Permission> permissions;
        private List<User> users;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder role(String role) {
            this.name = role;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder system(boolean system) {
            this.system = system;
            return this;
        }

        public Builder isSystem(boolean isSystem) {
            this.system = isSystem;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.active = isActive;
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

        public Builder permissions(Set<Permission> permissions) {
            this.permissions = permissions;
            return this;
        }

        public Builder users(List<User> users) {
            this.users = users;
            return this;
        }

        public Role build() {
            return new Role(id, uuid, name, description, system, active, deleted,
                    deletedAt, deletedBy, createdAt, createdBy, updatedAt, updatedBy,
                    version, permissions, users);
        }
    }
}
