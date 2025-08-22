package jp.co.azure_plus.session.controller;

import jakarta.validation.Valid;
import jp.co.azure_plus.session.form.CreateUserForm;
import jp.co.azure_plus.session.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService admin;

    @GetMapping
    public String list(Model model) {
        var users = admin.listUsers();
        Map<String, List<String>> authMap = new LinkedHashMap<>();
        for (var u : users) {
            authMap.put(u.username(), admin.listAuthorities(u.username()));
        }
        model.addAttribute("users", users);
        model.addAttribute("authMap", authMap);
        return "admin/users/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new CreateUserForm());
        return "admin/users/new";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("form") CreateUserForm form, BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) return "admin/users/new";
        admin.createUser(form.getUsername(), form.getPassword(), form.getRoles());
        ra.addFlashAttribute("msg", "ユーザーを作成しました");
        return "redirect:/admin/users";
    }

    @PostMapping("/{username}/roles")
    public String updateRoles(@PathVariable String username, @RequestParam List<String> roles, RedirectAttributes ra) {
        admin.replaceRoles(username, roles);
        ra.addFlashAttribute("msg", "権限を更新しました");
        return "redirect:/admin/users";
    }

    @PostMapping("/{username}/delete")
    public String delete(@PathVariable String username, RedirectAttributes ra) {
        admin.deleteUser(username);
        ra.addFlashAttribute("msg", "ユーザーを削除しました");
        return "redirect:/admin/users";
    }

    @PostMapping("/{username}/enabled")
    public String setEnabled(@PathVariable String username, @RequestParam boolean enabled, RedirectAttributes ra) {
        admin.setEnabled(username, enabled);
        ra.addFlashAttribute("msg", "有効/無効を更新しました");
        return "redirect:/admin/users";
    }
}

