package org.theoliverlear.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.theoliverlear.entity.User;
import org.theoliverlear.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @RequestMapping("/")
    public String user(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "user";
        } else {
            return "redirect:/account/";
        }
    }
}
