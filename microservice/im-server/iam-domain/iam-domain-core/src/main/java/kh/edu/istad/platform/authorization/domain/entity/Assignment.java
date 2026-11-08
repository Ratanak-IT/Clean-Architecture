package kh.edu.istad.platform.authorization.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.platform.authorization.domain.valueobject.AssignmentEffect;

import java.time.Instant;

public class Assignment extends BaseEntity<Long> {

    // Primary key
    private Long id;
    // FK -> users.id
    private Long userId;
    private User user;
    // FK -> roles.id
    private Long roleId;
    private Role role;
    // FK -> permissions.id
    private Long permissionId;
    private Permission permission;
    // GRANT or DENY
    private AssignmentEffect effect;
    // Assigned time
    private Instant assignedAt;
    // Actor sub
    private String assignedBy;
    // Optional expiry
    private Instant expiresAt;
    // Null = active
    private Instant revokedAt;
    // Actor sub
    private String revokedBy;
    // Why
    private String reason;
    public Assignment() {
    }
    public Assignment(Long id, Long userId, User user, Long roleId, Role role,
                      Long permissionId, Permission permission, AssignmentEffect effect,
                      Instant assignedAt, String assignedBy, Instant expiresAt,
                      Instant revokedAt, String revokedBy, String reason) {
        this.id = id;
        this.userId = userId;
        this.user = user;
        this.roleId = roleId;
        this.role = role;
        this.permissionId = permissionId;
        this.permission = permission;
        this.effect = effect;
        this.assignedAt = assignedAt;
        this.assignedBy = assignedBy;
        this.expiresAt = expiresAt;
        this.revokedAt = revokedAt;
        this.revokedBy = revokedBy;
        this.reason = reason;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
        if (role != null && role.getId() != null) {
            this.roleId = role.getId();
        }
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
        if (permission != null && permission.getId() != null) {
            this.permissionId = permission.getId();
        }
    }

    public AssignmentEffect getEffect() {
        return effect;
    }

    public void setEffect(AssignmentEffect effect) {
        this.effect = effect;
    }

    public void setEffect(String effect) {
        this.effect = effect != null ? AssignmentEffect.fromString(effect) : null;
    }

    public boolean isGrant() {
        return effect == AssignmentEffect.GRANT;
    }

    public boolean isDeny() {
        return effect == AssignmentEffect.DENY;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Instant assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
    }

    public String getRevokedBy() {
        return revokedBy;
    }

    public void setRevokedBy(String revokedBy) {
        this.revokedBy = revokedBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    // Helper domain logic
    public boolean isActive() {
        if (revokedAt != null) {
            return false;
        }
        return expiresAt == null || expiresAt.isAfter(Instant.now());
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long userId;
        private User user;
        private Long roleId;
        private Role role;
        private Long permissionId;
        private Permission permission;
        private AssignmentEffect effect = AssignmentEffect.GRANT;
        private Instant assignedAt;
        private String assignedBy;
        private Instant expiresAt;
        private Instant revokedAt;
        private String revokedBy;
        private String reason;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            if (user != null && user.getId() != null) {
                this.userId = user.getId();
            }
            return this;
        }

        public Builder roleId(Long roleId) {
            this.roleId = roleId;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            if (role != null && role.getId() != null) {
                this.roleId = role.getId();
            }
            return this;
        }

        public Builder permissionId(Long permissionId) {
            this.permissionId = permissionId;
            return this;
        }

        public Builder permission(Permission permission) {
            this.permission = permission;
            if (permission != null && permission.getId() != null) {
                this.permissionId = permission.getId();
            }
            return this;
        }

        public Builder effect(AssignmentEffect effect) {
            this.effect = effect;
            return this;
        }

        public Builder effect(String effect) {
            this.effect = effect != null ? AssignmentEffect.fromString(effect) : null;
            return this;
        }

        public Builder assignedAt(Instant assignedAt) {
            this.assignedAt = assignedAt;
            return this;
        }

        public Builder assignedBy(String assignedBy) {
            this.assignedBy = assignedBy;
            return this;
        }

        public Builder expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder revokedAt(Instant revokedAt) {
            this.revokedAt = revokedAt;
            return this;
        }

        public Builder revokedBy(String revokedBy) {
            this.revokedBy = revokedBy;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public Assignment build() {
            return new Assignment(id, userId, user, roleId, role, permissionId,
                    permission, effect, assignedAt, assignedBy, expiresAt,
                    revokedAt, revokedBy, reason);
        }
    }
}
