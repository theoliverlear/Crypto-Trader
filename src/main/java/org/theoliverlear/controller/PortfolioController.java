package org.theoliverlear.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.theoliverlear.entity.User;
import org.theoliverlear.service.CryptoTraderService;
import org.theoliverlear.service.CurrencyService;
import org.theoliverlear.service.PortfolioService;

@Controller
@RequestMapping("/portfolio")
public class PortfolioController extends CryptoTraderController {
    @Autowired
    public PortfolioController(CryptoTraderService cryptoTraderService,
                               CurrencyService currencyService,
                               PortfolioService portfolioService) {
        super(cryptoTraderService, currencyService, portfolioService);
    }
    @RequestMapping("/")
    public String builder(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/";
        }
        return "portfolio";
    }
}
