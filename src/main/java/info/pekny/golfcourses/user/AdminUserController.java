package info.pekny.golfcourses.user;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/user-list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("userForm", new UserCreateForm());
        model.addAttribute("roles", Role.values());
        return "admin/user-form";
    }

    @PostMapping("/create")
    public String create(@Valid UserCreateForm userForm, BindingResult result,
                         Model model, RedirectAttributes redirectAttributes) {
        if (userService.emailExists(userForm.getEmail())) {
            result.rejectValue("email", "duplicate", "Email already in use");
        }
        if (result.hasErrors()) {
            model.addAttribute("roles", Role.values());
            return "admin/user-form";
        }
        userService.create(userForm);
        redirectAttributes.addFlashAttribute("success", "User created successfully");
        return "redirect:/admin/users";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        UserEditForm form = new UserEditForm();
        form.setFullName(user.getFullName());
        form.setEmail(user.getEmail());
        form.setRole(user.getRole());
        form.setActive(user.isActive());
        model.addAttribute("userForm", form);
        model.addAttribute("userId", id);
        model.addAttribute("roles", Role.values());
        return "admin/user-form";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id, @Valid UserEditForm userForm,
                       BindingResult result, Model model,
                       RedirectAttributes redirectAttributes) {
        User existing = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        if (!existing.getEmail().equals(userForm.getEmail())
                && userService.emailExists(userForm.getEmail())) {
            result.rejectValue("email", "duplicate", "Email already in use");
        }
        if (userForm.getPassword() != null && !userForm.getPassword().isBlank()
                && userForm.getPassword().length() < 8) {
            result.rejectValue("password", "size", "Password must be at least 8 characters");
        }
        if (result.hasErrors()) {
            model.addAttribute("userId", id);
            model.addAttribute("roles", Role.values());
            return "admin/user-form";
        }
        userService.update(id, userForm);
        redirectAttributes.addFlashAttribute("success", "User updated successfully");
        return "redirect:/admin/users";
    }
}
