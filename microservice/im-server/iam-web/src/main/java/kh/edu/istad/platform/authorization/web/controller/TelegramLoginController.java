package kh.edu.istad.platform.authorization.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.edu.istad.platform.authorization.web.service.TelegramAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class TelegramLoginController {

    private final TelegramAuthService telegramAuthService;

    public TelegramLoginController(TelegramAuthService telegramAuthService) {
        this.telegramAuthService = telegramAuthService;
    }

    @GetMapping("/callback/telegram")
    public String handleTelegramCallback(
            @RequestParam Map<String, String> allParams,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return telegramAuthService.authenticateAndGetRedirect(allParams, request, response);
    }
}
