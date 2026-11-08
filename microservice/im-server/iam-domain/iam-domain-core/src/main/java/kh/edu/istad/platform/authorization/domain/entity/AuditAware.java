package kh.edu.istad.platform.authorization.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;

import java.time.Instant;

public abstract class AuditAware<ID> extends BaseEntity<ID> {

    private Instant createdAt;
    private String createdBy;

    private Instant updatedAt;
    private String updatedBy;

    private Instant deletedAt;
    private String deletedBy;

    public AuditAware() {
    }

    public AuditAware(Instant createdAt, String createdBy, Instant updatedAt,
                      String updatedBy, Instant deletedAt, String deletedBy) {
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }
}