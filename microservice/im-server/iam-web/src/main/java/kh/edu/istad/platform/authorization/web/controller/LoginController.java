package kh.edu.istad.platform.authorization.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping({"/", "/home"})
    public String home(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return "redirect:/login";
        }
        model.addAttribute("username", authentication.getName());
        return "home";
    }

    @GetMapping("/callback")
    @ResponseBody
    public String callback(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "error", required = false) String error
    ) {
        if (error != null) {
            return "<html><body style='font-family:sans-serif;padding:40px;background:#0f172a;color:#f87171'><h2>OAuth2 Authorization Error: " + error + "</h2></body></html>";
        }
        return "<html><body style='font-family:sans-serif;padding:40px;background:#0f172a;color:#f8fafc'><h2>Authorization Code Received:</h2><p style='color:#94a3b8'>Copy this code to exchange for Access Token:</p><textarea style='width:100%;max-width:700px;height:100px;background:#1e293b;color:#38bdf8;padding:12px;font-size:14px;border:1px solid #334155;border-radius:8px' readonly>" + (code != null ? code : "") + "</textarea></body></html>";
    }
}
