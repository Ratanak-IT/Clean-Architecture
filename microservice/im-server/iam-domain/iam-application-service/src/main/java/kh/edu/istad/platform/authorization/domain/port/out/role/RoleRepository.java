package kh.edu.istad.platform.authorization.domain.port.out.role;

import kh.edu.istad.platform.authorization.domain.dto.PageResult;
import kh.edu.istad.platform.authorization.domain.dto.role.command.AssignPermissionsToRoleCommand;
import kh.edu.istad.platform.authorization.domain.dto.role.command.RoleFilterQuery;
import kh.edu.istad.platform.authorization.domain.dto.role.result.PermissionAssignmentResult;
import kh.edu.istad.platform.authorization.domain.entity.Role;

import java.util.List;
import java.util.UUID;

public interface RoleRepository {
    Role save(Role role);
    Role findByUuid(UUID uuid);
    Role findByRole(String role);
    boolean existsByRole(String role);
    List<Role> findAll();
    PageResult<Role> findAll(RoleFilterQuery query);
    Role update(Role role);
    void deleteByUuid(UUID uuid);
    List<PermissionAssignmentResult> assignPermissions(AssignPermissionsToRoleCommand command);
    void revokePermission(UUID roleUuid, UUID permissionUuid, String revokedBy, String reason);
}
