package kh.edu.istad.imserver.feature.permission;


import kh.edu.istad.imserver.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionJpaRepository extends JpaRepository<Permission, Long> {
}
