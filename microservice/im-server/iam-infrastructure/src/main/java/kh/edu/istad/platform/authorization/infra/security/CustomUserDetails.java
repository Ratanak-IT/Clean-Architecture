package kh.edu.istad.platform.authorization.infra.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import kh.edu.istad.platform.authorization.persistence.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomUserDetails implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID uuid;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final Set<GrantedAuthority> authorities;

    @JsonCreator
    public CustomUserDetails(
            @JsonProperty("uuid") UUID uuid,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("enabled") boolean enabled,
            @JsonProperty("accountNonExpired") boolean accountNonExpired,
            @JsonProperty("accountNonLocked") boolean accountNonLocked,
            @JsonProperty("credentialsNonExpired") boolean credentialsNonExpired,
            @JsonProperty("authorities") Set<GrantedAuthority> authorities
    ) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.authorities = authorities != null ? Collections.unmodifiableSet(authorities) : Collections.emptySet();
    }

    public CustomUserDetails(UserEntity user) {
        this(
                user.getUuid(),
                user.getUsername(),
                user.getPasswordHash(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                extractAuthorities(user)
        );
    }

    public CustomUserDetails(UserEntity user, Set<GrantedAuthority> authorities) {
        this(
                user.getUuid(),
                user.getUsername(),
                user.getPasswordHash(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                authorities != null ? authorities : extractAuthorities(user)
        );
    }

    private static Set<GrantedAuthority> extractAuthorities(UserEntity user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        Instant now = Instant.now();
        if (user.getUserPermissions() != null) {
            user.getUserPermissions().forEach(userPermission -> {
                if (userPermission.getPermission() != null && userPermission.getPermission().getName() != null) {
                    if (userPermission.getRevokedAt() == null
                            && (userPermission.getExpiresAt() == null || userPermission.getExpiresAt().isAfter(now))) {
                        authorities.add(new SimpleGrantedAuthority(userPermission.getPermission().getName()));
                    }
                }
            });
        }
        if (user.getUserRoles() != null) {
            user.getUserRoles().forEach(userRole -> {
                if (userRole.getRole() != null && userRole.getRole().getName() != null) {
                    if (userRole.getRevokedAt() == null
                            && (userRole.getExpiresAt() == null || userRole.getExpiresAt().isAfter(now))) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getName()));
                        if (userRole.getRole().getRolePermissions() != null) {
                            userRole.getRole().getRolePermissions().forEach(rolePermission -> {
                                if (rolePermission.getPermission() != null && rolePermission.getPermission().getName() != null) {
                                    if (rolePermission.getRevokedAt() == null) {
                                        authorities.add(new SimpleGrantedAuthority(rolePermission.getPermission().getName()));
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    public UserEntity getUser() {
        return null;
    }

    public UUID getUuid() {
        return uuid;
    }
}
