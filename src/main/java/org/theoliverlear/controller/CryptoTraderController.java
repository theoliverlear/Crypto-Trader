package org.theoliverlear.controller;

//=================================-Imports-==================================
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.theoliverlear.entity.user.User;

@Controller
public class CryptoTraderController {
    //============================-Variables-=================================
    User currentUser;
    //=============================-Methods-==================================

    //--------------------------------Home------------------------------------
    @RequestMapping("/")
    public String home() {
        return "home";
    }
    //----------------------------Get-Started---------------------------------
    @RequestMapping("/get-started")
    public String getStarted(HttpSession session) {
        this.currentUser = (User) session.getAttribute("user");
        if (this.currentUser != null) {
            return "redirect:/account/";
        }
        return "get-started";
    }
}
