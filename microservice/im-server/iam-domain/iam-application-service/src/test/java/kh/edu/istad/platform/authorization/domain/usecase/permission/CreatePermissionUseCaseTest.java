package kh.edu.istad.platform.authorization.domain.usecase.permission;

import kh.edu.istad.common.domain.exception.ResourceAlreadyExistsException;
import kh.edu.istad.platform.authorization.domain.dto.permission.command.CreatePermissionCommand;
import kh.edu.istad.platform.authorization.domain.dto.permission.result.PermissionResult;
import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.mapper.PermissionDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.permission.PermissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePermissionUseCaseTest {

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private PermissionDomainMapper permissionDomainMapper;

    @Mock
    private CurrentActorProvider currentActorProvider;

    @InjectMocks
    private CreatePermissionUseCase useCase;

    @Test
    void createsCanonicalPermissionWhenItsNameIsAvailable() {
        CreatePermissionCommand command = new CreatePermissionCommand("Course", "Publish", "Publish courses");
        Permission permission = Permission.builder()
                .uuid(UUID.randomUUID())
                .name("course:publish")
                .resource("course")
                .action("publish")
                .build();
        PermissionResult result = PermissionResult.builder()
                .uuid(permission.getUuid())
                .name(permission.getName())
                .build();

        when(permissionRepository.existsByName("course:publish")).thenReturn(false);
        when(currentActorProvider.getCurrentActor()).thenReturn("admin");
        when(permissionDomainMapper.fromCreateCommand(command, "admin")).thenReturn(permission);
        when(permissionRepository.save(permission)).thenReturn(permission);
        when(permissionDomainMapper.toResult(permission)).thenReturn(result);

        PermissionResult actual = useCase.execute(command);

        assertEquals(result, actual);
        verify(permissionRepository).existsByName("course:publish");
        verify(permissionRepository).save(permission);
    }

    @Test
    void rejectsAnExistingPermissionNameBeforeWriting() {
        CreatePermissionCommand command = new CreatePermissionCommand("course", "publish", null);
        when(permissionRepository.existsByName("course:publish")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> useCase.execute(command));

        verify(permissionRepository, never()).save(any());
    }
}
