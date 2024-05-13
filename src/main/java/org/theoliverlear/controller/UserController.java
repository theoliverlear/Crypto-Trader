package org.theoliverlear.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.theoliverlear.comm.request.UserRequest;
import org.theoliverlear.comm.response.UserResponse;
import org.theoliverlear.entity.Portfolio;
import org.theoliverlear.entity.SafePassword;
import org.theoliverlear.entity.User;
import org.theoliverlear.service.PortfolioService;
import org.theoliverlear.service.UserService;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    UserService userService;
    PortfolioService portfolioService;
    User currentUser;
    @Autowired
    public UserController(UserService userService, PortfolioService portfolioService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
    }
    @RequestMapping("/")
    public String user(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "user";
        } else {
            this.currentUser = user;
            return "redirect:/account/";
        }
    }
    @RequestMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody UserRequest userRequest, HttpSession session) {
        String username = userRequest.getUsername();
        boolean userExists = this.userService.userExists(username);
        log.info("User exists: {}", userExists);
        if (userExists) {
            return new ResponseEntity<>(new UserResponse("User already exists"), HttpStatus.CONFLICT);
        } else {
            String password = userRequest.getPassword();
            SafePassword safePassword = new SafePassword(password);
            User user = new User(username, safePassword);
            Portfolio portfolio = new Portfolio(user);
            user.setPortfolio(portfolio);
            this.userService.saveUser(user);
            this.portfolioService.savePortfolio(portfolio);
            session.setAttribute("user", user);
            this.currentUser = user;
            log.info("User created: {}", user.getUsername());
            return ResponseEntity.ok(new UserResponse("User created"));
        }
    }
    @RequestMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest userRequest, HttpSession session) {
        String username = userRequest.getUsername();
        User user = this.userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.ok(new UserResponse("User not found"));
        } else {
            String password = userRequest.getPassword();
            if (this.userService.comparePassword(user, password)) {
                session.setAttribute("user", user);
                return ResponseEntity.ok(new UserResponse("Login successful"));
            } else {
                return new ResponseEntity<>(new UserResponse("Incorrect username or password"), HttpStatus.UNAUTHORIZED);
            }
        }
    }
}
