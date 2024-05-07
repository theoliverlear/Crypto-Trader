package org.theoliverlear.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.theoliverlear.comm.UserRequest;
import org.theoliverlear.comm.UserResponse;
import org.theoliverlear.entity.SafePassword;
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
    @RequestMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody UserRequest userRequest, HttpSession session) {
        String username = userRequest.getUsername();
        if (this.userService.userExists(username)) {
            return ResponseEntity.ok(new UserResponse("Username already exists"));
        } else {
            String password = userRequest.getPassword();
            SafePassword safePassword = new SafePassword(password);
            User user = new User(username, safePassword);
            this.userService.saveUser(user);
            session.setAttribute("user", user);
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
                return ResponseEntity.ok(new UserResponse("Incorrect password"));
            }
        }
    }
}
