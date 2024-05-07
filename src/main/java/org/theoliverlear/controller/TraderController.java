package org.theoliverlear.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.theoliverlear.entity.User;

@Controller
@RequestMapping("/trader")
public class TraderController {
    private User currentUser;
    @RequestMapping("/")
    public String trader(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/";
        } else {
            this.currentUser = user;
            return "trader";
        }
    }
//    @RequestMapping("/history")
//    public ResponseEntity</* make response class */> history() {
//
//    }
}
