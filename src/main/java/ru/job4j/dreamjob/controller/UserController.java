package ru.job4j.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/formAddUser")
    public String addPost(Model model) {
        model.addAttribute("user",
                new User(0,
                        "Заполните"));
        return "addUser";
    }

    @PostMapping("/createUser")
    public String createPost(Model model, @ModelAttribute User user) {
        System.out.println(user);
        Optional<User> regUser = userService.add(user);
        if (regUser.isEmpty()) {
            model.addAttribute("error",
                    "Ошибка регистрации: Пользователь с таким именем уже существует");
            return "fail";
        }
        return "redirect:/index";
    }

}
