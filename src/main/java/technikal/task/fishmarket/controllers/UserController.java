package technikal.task.fishmarket.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import technikal.task.fishmarket.models.UserEntity;
import technikal.task.fishmarket.services.UserRepository;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Страница со списком пользователей
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users/list";
    }

    // Форма для добавления нового пользователя
    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new UserEntity());
        return "users/new";
    }

    // Обработка формы
    @PostMapping
    public String createUser(@ModelAttribute UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    // Форма для редактирования существующего пользователя
    @GetMapping("/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        model.addAttribute("user", user);
        return "users/edit";
    }

    // Обработка обновления пользователя
    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserEntity user) {
        UserEntity existing = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        existing.setUsername(user.getUsername());
        // Если поле пароля не пустое — обновляем пароль
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        existing.setRole(user.getRole());
        userRepository.save(existing);
        return "redirect:/admin/users";
    }
}