package org.cryptotrader.api.controller;
//=================================-Imports-==================================
import jakarta.servlet.http.HttpSession;
import org.cryptotrader.api.services.PortfolioService;
import org.cryptotrader.api.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.cryptotrader.comm.request.PortfolioAssetRequest;
import org.cryptotrader.comm.response.AssetValueResponse;
import org.cryptotrader.comm.response.HasPortfolioResponse;
import org.cryptotrader.entity.portfolio.Portfolio;
import org.cryptotrader.entity.portfolio.PortfolioAsset;
import org.cryptotrader.entity.portfolio.PortfolioAssetHistory;
import org.cryptotrader.entity.portfolio.PortfolioHistory;
import org.cryptotrader.entity.user.ProductUser;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {
    //============================-Variables-=================================
    private ProductUser currentUser;
    private Portfolio portfolio = new Portfolio();
    private SessionService sessionService;
    private PortfolioService portfolioService;
    //===========================-Constructors-===============================
    @Autowired
    public PortfolioController(SessionService sessionService,
                               PortfolioService portfolioService) {
        this.sessionService = sessionService;
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
    public ResponseEntity<HasPortfolioResponse> emptyPortfolio(HttpSession session) {
        boolean userInSession = this.sessionService.userInSession(session);
        if (!userInSession) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<ProductUser> possibleSessionUser = this.sessionService.getUserFromSession(session);
        if (possibleSessionUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ProductUser sessionUser = possibleSessionUser.get();
        Portfolio portfolio = sessionUser.getPortfolio();
        if (portfolio == null) {
            return ResponseEntity.ok(new HasPortfolioResponse(false));
        } else {
            return ResponseEntity.ok(new HasPortfolioResponse(portfolio.isEmpty()));
        }
    }
    //------------------------Get-Portfolio-History---------------------------
    @RequestMapping("/history/get")
    public ResponseEntity<List<PortfolioHistory>> getPortfolioHistory(HttpSession session) {
        boolean userInSession = this.sessionService.userInSession(session);
        if (!userInSession) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<ProductUser> possibleSessionUser = this.sessionService.getUserFromSession(session);
        if (possibleSessionUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ProductUser sessionUser = possibleSessionUser.get();
        Portfolio portfolio = this.portfolioService.getPortfolioByUserId(sessionUser.getId());
        List<PortfolioHistory> portfolioHistory = this.portfolioService.getPortfolioHistory(portfolio);
        if (portfolioHistory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(portfolioHistory);
    }
    @RequestMapping("/history/get/asset")
    public ResponseEntity<List<PortfolioAssetHistory>> getPortfolioAssetHistory(HttpSession session) {
        ProductUser sessionUser = (ProductUser) session.getAttribute("user");
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
