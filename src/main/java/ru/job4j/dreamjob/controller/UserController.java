package ru.job4j.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UsersService;

import java.util.Optional;

@Controller
public class UserController {

    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/formAddUser")
    public String addPost(Model model) {
        model.addAttribute("user",
                new User(0,
                        "Заполните",
                        "Заполните",
                        "Заполните"));
        return "addUser";
    }

    @PostMapping("/createUser")
    public String createPost(Model model, @ModelAttribute User user) {
        Optional<User> regUser = usersService.add(user);
        if (regUser.isEmpty()) {
            model.addAttribute("error",
                    "Ошибка регистрации: Эта электронная почта уже используется");
            return "fail";
        }
        return "redirect:/index";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model, @RequestParam(name = "fail", required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user) {
        Optional<User> userDb = usersService.findUserByEmailAndPassword(
                user.getEmail(), user.getPassword()
        );
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        return "redirect:/index";
    }

}
