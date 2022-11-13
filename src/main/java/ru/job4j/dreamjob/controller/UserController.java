package ru.job4j.dreamjob.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static ru.job4j.dreamjob.controller.utils.ViewUtils.checkUserOrSetDefault;

@Controller
public class UserController {

    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/formAddUser")
    public String addUser(Model model, HttpSession session) {
        checkUserOrSetDefault(model, session);
        model.addAttribute("user",
                new User(0,
                        "Заполните",
                        "Заполните",
                        "Заполните"));
        return "addUser";
    }

    @PostMapping("/createUser")
    public String createUser(Model model, @ModelAttribute User user) {
        Optional<User> regUser = usersService.add(user);
        if (regUser.isEmpty()) {
            model.addAttribute("error",
                    "Ошибка регистрации: Эта электронная почта уже используется");
            return "fail";
        }
        return "redirect:/index";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model, HttpSession session, @RequestParam(name = "fail", required = false) Boolean fail) {
        checkUserOrSetDefault(model, session);
        model.addAttribute("fail", fail != null);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        Optional<User> userDb = usersService.findUserByEmailAndPassword(
                user.getEmail(), user.getPassword()
        );
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        req.getSession().setAttribute("user", userDb.get());
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }



}
