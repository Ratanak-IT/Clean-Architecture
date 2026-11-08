package kh.edu.istad.platform.authorization.web.service;

import kh.edu.istad.platform.authorization.domain.dto.user.command.RegisterUserCommand;
import kh.edu.istad.platform.authorization.domain.usecase.user.RegisterUserUseCase;
import kh.edu.istad.platform.authorization.web.dto.RegisterWebForm;
import org.springframework.stereotype.Service;

@Service
public class RegistrationWebService {

    private final RegisterUserUseCase registerUserUseCase;

    public RegistrationWebService(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
    }

    public void register(RegisterWebForm form) {
        if (form.password() != null && !form.password().equals(form.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        RegisterUserCommand command = RegisterUserCommand.builder()
                .username(form.username().trim())
                .email(form.email().trim())
                .password(form.password())
                .confirmPassword(form.confirmPassword())
                .givenName(form.firstName() != null && !form.firstName().isBlank() ? form.firstName().trim() : null)
                .familyName(form.lastName() != null && !form.lastName().isBlank() ? form.lastName().trim() : null)
                .build();

        registerUserUseCase.execute(command);
    }
}
