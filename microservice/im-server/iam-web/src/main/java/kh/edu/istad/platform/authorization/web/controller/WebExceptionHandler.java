package kh.edu.istad.platform.authorization.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(basePackages = "kh.edu.istad.platform.authorization.web.controller")
public class WebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(WebExceptionHandler.class);

    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException ex, Model model) {
        log.warn("Validation binding error: {}", ex.getMessage());
        model.addAttribute("form", ex.getTarget());
        model.addAttribute("errors", ex.getBindingResult());
        return "register";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        log.warn("User input error: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        String referer = request.getHeader("Referer");
        return referer != null ? "redirect:" + referer : "redirect:/";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        log.error("Unhandled web exception occurred", ex);
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        String referer = request.getHeader("Referer");
        return referer != null ? "redirect:" + referer : "redirect:/";
    }
}

