package org.theoliverlear.controller;
//=================================-Imports-==================================
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.theoliverlear.comm.request.PortfolioAssetRequest;
import org.theoliverlear.comm.response.AssetValueResponse;
import org.theoliverlear.entity.portfolio.Portfolio;
import org.theoliverlear.entity.portfolio.PortfolioAsset;
import org.theoliverlear.entity.portfolio.PortfolioAssetHistory;
import org.theoliverlear.entity.portfolio.PortfolioHistory;
import org.theoliverlear.entity.user.User;
import org.theoliverlear.service.PortfolioService;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {
    //============================-Variables-=================================
    private User currentUser;
    private Portfolio portfolio = new Portfolio();
    private PortfolioService portfolioService;
    //===========================-Constructors-===============================
    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }
    //=============================-Methods-==================================

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
    //------------------------Get-Portfolio-History---------------------------
    @RequestMapping("/history/get")
    public ResponseEntity<List<PortfolioHistory>> getPortfolioHistory(HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        this.currentUser = sessionUser;
        this.portfolio = this.portfolioService.getPortfolioByUserId(this.currentUser.getId());
        List<PortfolioHistory> portfolioHistory = this.portfolioService.getPortfolioHistory(this.portfolio);
        return ResponseEntity.ok(portfolioHistory);
    }
    @RequestMapping("/history/get/asset")
    public ResponseEntity<List<PortfolioAssetHistory>> getPortfolioAssetHistory(HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        this.currentUser = sessionUser;
        this.portfolio = this.portfolioService.getPortfolioByUserId(this.currentUser.getId());
        List<PortfolioAssetHistory> portfolioAssetHistory = this.portfolioService.getPortfolioAssetHistory(this.portfolio);
        return ResponseEntity.ok(portfolioAssetHistory);
    }
    @RequestMapping("/history/get/asset/{currencyName}")
    public ResponseEntity<List<PortfolioAssetHistory>> getPortfolioAssetHistoryByCurrency(@PathVariable String currencyName) {
        return null;
    }
    //------------------------Get-Portfolio-Profit----------------------------
    @RequestMapping("/history/profit")
    public ResponseEntity<AssetValueResponse> getPortfolioProfit() {
        this.portfolio = this.portfolioService.getPortfolioByUserId(this.currentUser.getId());
        double profit = this.portfolioService.getPortfolioProfit(this.portfolio);
        AssetValueResponse profitResponse = new AssetValueResponse(profit);
        return ResponseEntity.ok(profitResponse);
    }
    //------------------Get-Portfolio-Profit-By-Currency----------------------
    @RequestMapping("/history/profit/{currencyName}")
    public ResponseEntity<AssetValueResponse> getPortfolioProfitByCurrency(@PathVariable String currencyName) {
        this.portfolio = this.portfolioService.getPortfolioByUserId(this.currentUser.getId());
        PortfolioAsset portfolioAsset = this.portfolioService.getPortfolioAssetByCurrencyName(this.portfolio, currencyName);
        double profit = this.portfolioService.getPortfolioAssetProfit(portfolioAsset);
        AssetValueResponse profitResponse = new AssetValueResponse(profit);
        return ResponseEntity.ok(profitResponse);
    }
}
