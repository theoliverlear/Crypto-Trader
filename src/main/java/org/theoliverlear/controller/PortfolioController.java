package org.theoliverlear.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.theoliverlear.comm.request.PortfolioAssetRequest;
import org.theoliverlear.entity.Portfolio;
import org.theoliverlear.entity.User;
import org.theoliverlear.service.PortfolioService;

@Controller
@RequestMapping("/portfolio")
public class PortfolioController {
    User currentUser;
    Portfolio portfolio = new Portfolio();
    PortfolioService portfolioService;
    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }
    @RequestMapping("/")
    public String builder(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/";
        }
        this.currentUser = user;
        this.portfolio = this.portfolioService.getPortfolio(user.getId());
        return "portfolio";
    }
    @RequestMapping("/get")
    public ResponseEntity<Portfolio> getPortfolio() {
        return ResponseEntity.ok(this.portfolio);
    }
    @RequestMapping("/add")
    public ResponseEntity<String> addPortfolioAsset(@RequestBody PortfolioAssetRequest portfolioAssetRequest) {
        this.portfolioService.addAssetToPortfolio(this.portfolio, portfolioAssetRequest);
        return ResponseEntity.ok("Asset added to portfolio");
    }
}
