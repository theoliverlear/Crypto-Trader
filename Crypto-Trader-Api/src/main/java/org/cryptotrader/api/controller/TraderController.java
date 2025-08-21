package org.cryptotrader.api.controller;
//=================================-Imports-==================================
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.cryptotrader.entity.user.User;

@Controller
@RequestMapping("/api/trader")
public class TraderController {
    //============================-Variables-=================================
    private User currentUser;
    //=============================-Methods-==================================

}
