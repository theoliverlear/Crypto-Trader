package org.theoliverlear.controller;
//=================================-Imports-==================================
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.theoliverlear.entity.user.User;
import org.theoliverlear.service.PortfolioService;
import org.theoliverlear.service.ProductUserService;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    //============================-Variables-=================================
    private ProductUserService productUserService;
    private PortfolioService portfolioService;
    private User currentUser;
    //===========================-Constructors-===============================
    @Autowired
    public UserController(ProductUserService productUserService, PortfolioService portfolioService) {
        this.productUserService = productUserService;
        this.portfolioService = portfolioService;
    }
    //=============================-Methods-==================================

}
