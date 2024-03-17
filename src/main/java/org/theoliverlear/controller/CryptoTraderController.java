package org.theoliverlear.controller;

import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.theoliverlear.CryptoTrader;
import org.theoliverlear.comm.UserRequest;
import org.theoliverlear.entity.User;
import org.theoliverlear.service.CryptoTraderService;

@Controller
public class CryptoTraderController {
    User currentUser;
    CryptoTrader cryptoTrader;
    @Autowired
    CryptoTraderService cryptoTraderService;
    @RequestMapping("/")
    public String index() {
        return "index";
    }
    @RequestMapping("/signup")
    public ResponseEntity<String> signup(@ModelAttribute UserRequest userRequest) {
        User newUser = new User(userRequest.getUsername(), userRequest.getPassword(), 1L);
        this.cryptoTraderService.saveUser(newUser);
        return ResponseEntity.ok("User " + userRequest.getUsername() + " has been signed up!");
    }
}
