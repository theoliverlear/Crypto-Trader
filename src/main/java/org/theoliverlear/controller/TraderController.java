package org.theoliverlear.controller;
//=================================-Imports-==================================
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.theoliverlear.entity.user.User;

@Controller
@RequestMapping("/trader")
public class TraderController {
    //============================-Variables-=================================
    private User currentUser;
    //=============================-Methods-==================================

    //-------------------------------Trader-----------------------------------
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
}
