package org.cryptotrader.api.controller;
//=================================-Imports-==================================
import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.api.library.services.PortfolioService;
import org.cryptotrader.api.library.services.ProductUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.cryptotrader.api.library.entity.user.User;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    //============================-Variables-=================================
    private final ProductUserService productUserService;
    private final PortfolioService portfolioService;
    //===========================-Constructors-===============================
    @Autowired
    public UserController(ProductUserService productUserService, PortfolioService portfolioService) {
        this.productUserService = productUserService;
        this.portfolioService = portfolioService;
    }
    //=============================-Methods-==================================

}
