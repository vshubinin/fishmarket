package technikal.task.fishmarket.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}