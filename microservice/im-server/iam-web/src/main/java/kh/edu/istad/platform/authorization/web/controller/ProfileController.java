package kh.edu.istad.platform.authorization.web.controller;

import kh.edu.istad.platform.authorization.web.service.ProfileWebService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileWebService profileWebService;

    public ProfileController(ProfileWebService profileWebService) {
        this.profileWebService = profileWebService;
    }

    // 1. Edit Profile
    @GetMapping("/edit")
    public String showEditProfile(Principal principal, Model model) {
        model.addAttribute("user", profileWebService.getMyProfile());
        model.addAttribute("username", principal.getName());
        return "profile-edit";
    }

    @PostMapping("/edit")
    public String handleEditProfile(
            @RequestParam(value = "givenName", required = false) String givenName,
            @RequestParam(value = "familyName", required = false) String familyName,
            @RequestParam(value = "nickname", required = false) String nickname,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "locale", required = false) String locale,
            RedirectAttributes redirectAttributes
    ) {
        profileWebService.updateProfile(givenName, familyName, nickname, phoneNumber, locale);
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/profile/edit";
    }

    // 2. Change Password
    @GetMapping("/password")
    public String showChangePassword(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "change-password";
    }

    @PostMapping("/password")
    public String handleChangePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes
    ) {
        profileWebService.changePassword(currentPassword, newPassword, confirmPassword);
        redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        return "redirect:/profile/password";
    }

    // 3. Linked Accounts
    @GetMapping("/accounts")
    public String showLinkedAccounts(Principal principal, Model model) {
        model.addAttribute("identities", profileWebService.getMyIdentities());
        model.addAttribute("username", principal.getName());
        return "linked-accounts";
    }

    @PostMapping("/accounts/{id}/unlink")
    public String handleUnlinkAccount(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        profileWebService.unlinkIdentity(id);
        redirectAttributes.addFlashAttribute("successMessage", "Account unlinked successfully!");
        return "redirect:/profile/accounts";
    }

    // 4. Connected Applications
    @GetMapping("/apps")
    public String showConnectedApps(Principal principal, Model model) {
        model.addAttribute("apps", profileWebService.getConnectedApps(principal.getName()));
        model.addAttribute("username", principal.getName());
        return "connected-apps";
    }

    @PostMapping("/apps/revoke")
    public String handleRevokeApp(
            @RequestParam("clientId") String registeredClientId,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        profileWebService.revokeAppConsent(registeredClientId, principal.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Application authorization revoked!");
        return "redirect:/profile/apps";
    }
}
