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
    PortfolioService portfolioService;
    @Autowired
    public CryptoTraderController(CryptoTraderService cryptoTraderService,
                                  CurrencyService currencyService,
                                  PortfolioService portfolioService) {
        this.cryptoTraderService = cryptoTraderService;
        this.currencyService = currencyService;
        this.portfolioService = portfolioService;
    }
    //=============================-Methods-==================================

    //--------------------------------Home------------------------------------
    @RequestMapping("/")
    public String home() {
        return "home";
    }
    //----------------------------Get-Started---------------------------------
    @RequestMapping("/get-started")
    public String getStarted() {
        return "get-started";
    }
}
