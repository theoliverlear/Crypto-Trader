package org.theoliverlear.controller;

import jakarta.servlet.http.HttpSession;
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
public class UserController {
    UserService userService;
    PortfolioService portfolioService;
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
            return "redirect:/account/";
        }
    }
    @RequestMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody UserRequest userRequest, HttpSession session) {
        String username = userRequest.getUsername();
        boolean userExists = this.userService.userExists(username);
        System.out.println("User exists: " + userExists);
        if (userExists) {
            return new ResponseEntity<>(new UserResponse("User already exists"), HttpStatus.CONFLICT);
        } else {
            String password = userRequest.getPassword();
            SafePassword safePassword = new SafePassword(password);
            User user = new User(username, safePassword);
            this.userService.saveUser(user);
            Portfolio newUserPortfolio = new Portfolio();
            this.portfolioService.savePortfolio(newUserPortfolio);
            User updatedUser = this.userService.getUserByUsername(username);
            updatedUser.setPortfolio(newUserPortfolio);
            this.userService.saveUser(updatedUser);
            this.portfolioService.savePortfolio(newUserPortfolio);
            User sessionUser = this.userService.getUserByUsername(username);
            session.setAttribute("user", sessionUser);
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
