package kh.edu.istad.platform.authorization.web.i18n;

import org.springframework.context.MessageSource;
import java.util.Locale;

public record I18nHelper(MessageSource messageSource, Locale locale) {

    public String get(String code, Object... args) {
        if (code == null) return "";
        try {
            return messageSource.getMessage(code, args, code, locale != null ? locale : Locale.ENGLISH);
        } catch (Exception ex) {
            return code;
        }
    }

    public String t(String code, Object... args) {
        return get(code, args);
    }
}

