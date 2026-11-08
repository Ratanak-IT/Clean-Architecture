package kh.edu.istad.platform.authorization.infra.security;

import kh.edu.istad.platform.authorization.persistence.entity.UserEntity;
import kh.edu.istad.platform.authorization.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity existing = userJpaRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with: " + username));

        Set<GrantedAuthority> authorities = extractAuthorities(existing);

        return new org.springframework.security.core.userdetails.User(
                existing.getUsername(),
                existing.getPasswordHash(),
                existing.isEnabled(),
                existing.isAccountNonExpired(),
                existing.isCredentialsNonExpired(),
                existing.isAccountNonLocked(),
                authorities
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
}
