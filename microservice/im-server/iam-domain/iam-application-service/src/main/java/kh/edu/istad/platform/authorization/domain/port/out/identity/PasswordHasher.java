package kh.edu.istad.platform.authorization.domain.port.out.identity;

// Implemented by iam-infrastructure using Spring Security's PasswordEncoder.
public interface PasswordHasher {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String passwordHash);
}

