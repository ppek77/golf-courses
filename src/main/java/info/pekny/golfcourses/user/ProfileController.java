package info.pekny.golfcourses.user;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow();
        ProfileEditForm form = new ProfileEditForm();
        form.setFullName(user.getFullName());
        model.addAttribute("profileForm", form);
        return "profile";
    }

    @PostMapping
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @Valid ProfileEditForm profileForm,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (profileForm.getNewPassword() != null && !profileForm.getNewPassword().isBlank()
                && profileForm.getNewPassword().length() < 8) {
            result.rejectValue("newPassword", "size", "Password must be at least 8 characters");
        }
        if (result.hasErrors()) {
            return "profile";
        }
        try {
            userService.updateProfile(userDetails.getUsername(), profileForm);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully");
        } catch (IllegalArgumentException e) {
            result.rejectValue("currentPassword", "invalid", e.getMessage());
            return "profile";
        }
        return "redirect:/profile";
    }
}
