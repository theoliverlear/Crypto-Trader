package org.theoliverlear.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @RequestMapping("/builder")
    public String builder() {
        return "portfolio-builder";
    }
}
