package kh.edu.istad.platform.authorization.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "identities")
public class IdentityEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(length = 20, nullable = false)
    private String provider;

    @Column(name = "provider_subject", nullable = false)
    private String providerSubject;

    private String email;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Column(name = "display_name", length = 150)
    private String displayName;

    @Column(length = 100)
    private String username;

    @Column(name = "picture_url", length = 500)
    private String pictureUrl;

    @Column(columnDefinition = "TEXT")
    private String attributes;

    @Column(name = "linked_at")
    private Instant linkedAt;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Version
    private Long version;
}
