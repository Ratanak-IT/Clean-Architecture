package kh.edu.istad.platform.authorization.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.PermissionId;
import kh.edu.istad.platform.authorization.domain.exception.AuthorizationDomainException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Permission extends AuditAware<Long> {

    // Internal primary key
    private Long id;
    // Public identifier
    private UUID uuid;
    // resource:action, e.g. user:read
    private String name;
    // Protected resource, e.g. user, role, order
    private String resource;
    // Action, e.g. create, read, update, delete
    private String action;
    // Human-readable meaning
    private String description;
    // Built-in, protected
    private boolean system;
    // Active flag
    private boolean active;
    // Soft delete
    private boolean deleted;
    // Optimistic lock
    private Long version;
    // Associated users & roles
    private List<User> users;
    private List<Role> roles;
    public Permission() {
    }
    public Permission(Long id, UUID uuid, String name, String resource, String action,
                      String description, boolean system, boolean active, boolean deleted,
                      Instant deletedAt, String deletedBy, Instant createdAt, String createdBy,
                      Instant updatedAt, String updatedBy, Long version, List<User> users,
                      List<Role> roles) {
        super(createdAt, createdBy, updatedAt, updatedBy, deletedAt, deletedBy);
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.resource = resource;
        this.action = action;
        this.description = description;
        this.system = system;
        this.active = active;
        this.deleted = deleted;
        this.version = version;
        this.users = users;
        this.roles = roles;
    }

    // System permissions cannot be changed or deleted.
    public void validateCanModify() {
        if (this.system) {
            throw new AuthorizationDomainException("System permission cannot be changed or deleted: " + this.name);
        }
    }

    // Inactive permissions cannot be assigned.
    public void validateCanAssign() {
        if (!this.active || this.deleted) {
            throw new AuthorizationDomainException("Inactive or deleted permission cannot be assigned: " + this.name);
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

    public PermissionId getPermissionId() {
        return uuid != null ? new PermissionId(uuid) : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Alias for permission string compatibility
    public String getPermission() {
        return name;
    }

    public void setPermission(String permission) {
        this.name = permission;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private UUID uuid;
        private String name;
        private String resource;
        private String action;
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
        private List<User> users;
        private List<Role> roles;

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

        public Builder permission(String permission) {
            this.name = permission;
            return this;
        }

        public Builder resource(String resource) {
            this.resource = resource;
            return this;
        }

        public Builder action(String action) {
            this.action = action;
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

        public Builder users(List<User> users) {
            this.users = users;
            return this;
        }

        public Builder roles(List<Role> roles) {
            this.roles = roles;
            return this;
        }

        public Permission build() {
            return new Permission(id, uuid, name, resource, action, description,
                    system, active, deleted, deletedAt, deletedBy, createdAt,
                    createdBy, updatedAt, updatedBy, version, users, roles);
        }
    }
}
