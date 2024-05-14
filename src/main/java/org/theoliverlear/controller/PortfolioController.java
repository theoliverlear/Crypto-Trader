package org.theoliverlear.controller;
//=================================-Imports-==================================
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.theoliverlear.comm.request.PortfolioAssetRequest;
import org.theoliverlear.entity.portfolio.Portfolio;
import org.theoliverlear.entity.user.User;
import org.theoliverlear.service.PortfolioService;

@Controller
@RequestMapping("/portfolio")
public class PortfolioController {
    //============================-Variables-=================================
    User currentUser;
    Portfolio portfolio = new Portfolio();
    PortfolioService portfolioService;
    //===========================-Constructors-===============================
    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }
    //=============================-Methods-==================================

    //-----------------------------Portfolio----------------------------------
    @RequestMapping("/")
    public String portfolio(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/";
        }
        this.currentUser = user;
        this.portfolio = this.portfolioService.getPortfolioByUserId(user.getId());
        return "portfolio";
    }
    //---------------------------Get-Portfolio--------------------------------
    @RequestMapping("/get")
    public ResponseEntity<Portfolio> getPortfolio() {
        this.portfolio = this.portfolioService.getPortfolioByUserId(this.currentUser.getId());
        return ResponseEntity.ok(this.portfolio);
    }
    //------------------------Add-Portfolio-Asset-----------------------------
    @RequestMapping("/add")
    public ResponseEntity<String> addPortfolioAsset(@RequestBody PortfolioAssetRequest portfolioAssetRequest) {
        this.portfolioService.addAssetToPortfolio(this.portfolio, portfolioAssetRequest);
        return ResponseEntity.ok("Asset added to portfolio");
    }
    //-------------------------Is-Empty-Portfolio-----------------------------
    @RequestMapping("/empty")
    public ResponseEntity<String> emptyPortfolio() {
        if (this.portfolio.isEmpty()) {
            return ResponseEntity.ok("true");
        } else {
            return ResponseEntity.ok("false");
        }
    }
}
