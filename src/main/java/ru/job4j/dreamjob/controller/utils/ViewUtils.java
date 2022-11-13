package ru.job4j.dreamjob.controller.utils;

import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.User;

import javax.servlet.http.HttpSession;

public class ViewUtils {

    public static final String GUEST = "Гость";

    public static void checkUserOrSetDefault(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User(0, GUEST, null, null);
        }
        model.addAttribute("user", user);
    }
}
