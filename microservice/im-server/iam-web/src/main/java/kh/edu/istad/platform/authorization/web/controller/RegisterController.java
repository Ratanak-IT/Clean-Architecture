package kh.edu.istad.platform.authorization.web.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.authorization.web.dto.RegisterWebForm;
import kh.edu.istad.platform.authorization.web.service.RegistrationWebService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    private final RegistrationWebService registrationWebService;

    public RegisterController(RegistrationWebService registrationWebService) {
        this.registrationWebService = registrationWebService;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("form", new RegisterWebForm("", "", "", "", "", ""));
        return "register";
    }

    @PostMapping("/register")
    public String handleRegistration(
            @Valid @ModelAttribute("form") RegisterWebForm form,
            RedirectAttributes redirectAttributes
    ) {
        registrationWebService.register(form);
        redirectAttributes.addFlashAttribute("registered", "true");
        return "redirect:/login";
    }
}
