package kh.edu.istad.platform.authorization.domain.usecase.permission;

import kh.edu.istad.platform.authorization.domain.dto.PageResult;
import kh.edu.istad.platform.authorization.domain.dto.permission.command.PermissionFilterQuery;
import kh.edu.istad.platform.authorization.domain.dto.permission.result.PermissionResult;
import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.mapper.PermissionDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.permission.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListPermissionsUseCase {

    private final PermissionRepository permissionRepository;
    private final PermissionDomainMapper permissionDomainMapper;

    @Transactional(readOnly = true)
    public PageResult<PermissionResult> execute(PermissionFilterQuery query) {
        PageResult<Permission> page = permissionRepository.findAll(query);
        List<PermissionResult> content = page.content().stream()
                .map(permissionDomainMapper::toResult)
                .toList();
        return PageResult.of(content, page.pageNumber(), page.pageSize(), page.totalElements());
    }
}
