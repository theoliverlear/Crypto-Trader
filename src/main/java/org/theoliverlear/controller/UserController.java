package org.theoliverlear.controller;
//=================================-Imports-==================================
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.theoliverlear.comm.request.UserRequest;
import org.theoliverlear.comm.response.UserResponse;
import org.theoliverlear.entity.portfolio.Portfolio;
import org.theoliverlear.entity.user.SafePassword;
import org.theoliverlear.entity.user.User;
import org.theoliverlear.service.PortfolioService;
import org.theoliverlear.service.UserService;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    //============================-Variables-=================================
    private UserService userService;
    private PortfolioService portfolioService;
    private User currentUser;
    //===========================-Constructors-===============================
    @Autowired
    public UserController(UserService userService, PortfolioService portfolioService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
    }
    //=============================-Methods-==================================

}
