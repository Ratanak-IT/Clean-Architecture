package kh.edu.istad.platform.authorization.infra.security;

import kh.edu.istad.platform.authorization.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final UserEntity user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (user.getUserPermissions() != null) {
            user.getUserPermissions().forEach(userPermission -> {
                if (userPermission.getPermission() != null && userPermission.getPermission().getName() != null) {
                    authorities.add(new SimpleGrantedAuthority(userPermission.getPermission().getName()));
                }
            });
        }
        if (user.getUserRoles() != null) {
            user.getUserRoles().forEach(userRole -> {
                if (userRole.getRole() != null && userRole.getRole().getName() != null) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getName()));
                    if (userRole.getRole().getRolePermissions() != null) {
                        userRole.getRole().getRolePermissions().forEach(rolePermission -> {
                            if (rolePermission.getPermission() != null && rolePermission.getPermission().getName() != null) {
                                authorities.add(new SimpleGrantedAuthority(rolePermission.getPermission().getName()));
                            }
                        });
                    }
                }
            });
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
