package org.theoliverlear.controller;

//=================================-Imports-==================================
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.theoliverlear.comm.UserRequest;
import org.theoliverlear.entity.User;
import org.theoliverlear.service.CryptoTraderService;
import org.theoliverlear.service.CurrencyService;
import org.theoliverlear.service.PortfolioService;

@Controller
public class CryptoTraderController {
    //============================-Variables-=================================
    User currentUser;
    CryptoTraderService cryptoTraderService;
    CurrencyService currencyService;
    @Autowired
    public CryptoTraderController(CryptoTraderService cryptoTraderService,
                                  CurrencyService currencyService,
                                  PortfolioService portfolioService) {
        this.cryptoTraderService = cryptoTraderService;
        this.currencyService = currencyService;
    }
    //=============================-Methods-==================================

    //-------------------------------Index------------------------------------
    @RequestMapping("/")
    public String home() {
        return "home";
    }
    //-------------------------------Login------------------------------------
    @RequestMapping("/signup")
    public ResponseEntity<String> signup(@ModelAttribute UserRequest userRequest) {
        User newUser = new User(userRequest.getUsername(), userRequest.getPassword(), 1L);
        this.cryptoTraderService.saveUser(newUser);
        return ResponseEntity.ok("User " + userRequest.getUsername() + " has been signed up!");
    }
}
