package kh.edu.istad.platform.authorization.infra.security;

import kh.edu.istad.platform.authorization.domain.dto.identity.result.AuthUserResult;
import kh.edu.istad.platform.authorization.domain.usecase.user.LoadUserForAuthenticationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final LoadUserForAuthenticationUseCase loadUserForAuthenticationUseCase;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUserResult user;
        try {
            user = loadUserForAuthenticationUseCase.execute(username);
        } catch (Exception ex) {
            throw new UsernameNotFoundException("User not found with: " + username, ex);
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found with: " + username);
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        if (user.roles() != null) {
            user.roles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        }
        if (user.permissions() != null) {
            user.permissions().forEach(perm -> authorities.add(new SimpleGrantedAuthority(perm)));
        }

        return new CustomUserDetails(
                user.userUuid(),
                user.username(),
                user.passwordHash(),
                user.enabled(),
                user.accountNonExpired(),
                user.accountNonLocked(),
                user.credentialsNonExpired(),
                authorities
        );
    }
}