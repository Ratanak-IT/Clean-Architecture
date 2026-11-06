package kh.edu.istad.imserver.feature.role;

import kh.edu.istad.imserver.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {
}
