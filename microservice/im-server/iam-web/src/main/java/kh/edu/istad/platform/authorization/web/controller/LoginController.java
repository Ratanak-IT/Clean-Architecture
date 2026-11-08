package kh.edu.istad.platform.authorization.web.controller;

import kh.edu.istad.platform.authorization.web.dto.DashboardView;
import kh.edu.istad.platform.authorization.web.service.DashboardWebService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    private final DashboardWebService dashboardWebService;

    @Value("${telegram.bot-username:}")
    private String telegramBotUsername;

    public LoginController(DashboardWebService dashboardWebService) {
        this.dashboardWebService = dashboardWebService;
    }

    @GetMapping("/login")
    public String login(
            Model model,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registered", required = false) String registered
    ) {
        model.addAttribute("telegramBotUsername", telegramBotUsername);
        model.addAttribute("error", error);
        model.addAttribute("logout", logout);
        model.addAttribute("registered", registered);
        return "login";
    }

    @GetMapping({"/", "/home"})
    public String home(Authentication authentication, Model model) {
        DashboardView dashboard = dashboardWebService.getDashboard(authentication);
        model.addAttribute("user", dashboard.user());
        model.addAttribute("roles", dashboard.roles());
        model.addAttribute("permissions", dashboard.permissions());
        model.addAttribute("username", dashboard.username());
        model.addAttribute("claims", dashboard.claims());
        return "index";
    }

    @GetMapping("/callback")
    @ResponseBody
    public String callback(@RequestParam(value = "code", required = false) String code) {
        return "<html><body style='font-family:sans-serif;padding:40px;background:#0f172a;color:#f8fafc'><h2>Authorization Code Received:</h2><p style='color:#94a3b8'>Copy this code to exchange for Access Token:</p><textarea style='width:100%;max-width:700px;height:100px;background:#1e293b;color:#38bdf8;padding:12px;font-size:14px;border:1px solid #334155;border-radius:8px' readonly>" + code + "</textarea></body></html>";
    }
}
