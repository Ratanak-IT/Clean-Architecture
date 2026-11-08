package kh.edu.istad.platform.authorization.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import kh.edu.istad.platform.authorization.web.i18n.I18nHelper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Locale;

@ControllerAdvice
public class GlobalWebAdvice {

    private final MessageSource messageSource;

    public GlobalWebAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ModelAttribute("msg")
    public I18nHelper msg() {
        Locale locale = LocaleContextHolder.getLocale();
        return new I18nHelper(messageSource, locale);
    }

    @ModelAttribute("currentLang")
    public String currentLang() {
        Locale locale = LocaleContextHolder.getLocale();
        return locale != null ? locale.getLanguage() : "en";
    }

    @ModelAttribute("_csrf")
    public CsrfToken csrfToken(HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token == null) {
            token = (CsrfToken) request.getAttribute("_csrf");
        }
        return token;
    }
}

