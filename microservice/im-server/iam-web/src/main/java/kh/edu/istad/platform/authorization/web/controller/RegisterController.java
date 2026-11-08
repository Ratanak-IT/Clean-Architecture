package kh.edu.istad.platform.authorization.web.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.authorization.domain.dto.user.command.RegisterUserCommand;
import kh.edu.istad.platform.authorization.domain.usecase.user.RegisterUserUseCase;
import kh.edu.istad.platform.authorization.web.dto.RegisterWebForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterUserUseCase registerUserUseCase;

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new RegisterWebForm("", "", "", "", "", ""));
        }
        return "register";
    }

    @PostMapping("/register")
    public String handleRegistration(
            @Valid @ModelAttribute("form") RegisterWebForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (form.password() != null && !form.password().equals(form.confirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            RegisterUserCommand command = RegisterUserCommand.builder()
                    .username(form.username().trim())
                    .email(form.email().trim())
                    .password(form.password())
                    .confirmPassword(form.confirmPassword())
                    .givenName(form.firstName() != null && !form.firstName().isBlank() ? form.firstName().trim() : null)
                    .familyName(form.lastName() != null && !form.lastName().isBlank() ? form.lastName().trim() : null)
                    .build();

            registerUserUseCase.execute(command);
            return "redirect:/login?registered=true";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "register";
        } catch (Exception ex) {
            log.error("Registration failed unexpectedly for username={}", form.username(), ex);
            model.addAttribute("errorMessage", "An error occurred while creating your account. Please try again.");
            return "register";
        }
    }
}

