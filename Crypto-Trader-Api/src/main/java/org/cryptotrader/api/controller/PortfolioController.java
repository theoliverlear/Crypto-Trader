package org.cryptotrader.api.controller;
//=================================-Imports-==================================
import jakarta.servlet.http.HttpSession;
import org.cryptotrader.api.library.services.PortfolioService;
import org.cryptotrader.api.library.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.cryptotrader.api.library.communication.request.PortfolioAssetRequest;
import org.cryptotrader.api.library.communication.response.AssetValueResponse;
import org.cryptotrader.api.library.communication.response.HasPortfolioResponse;
import org.cryptotrader.api.library.entity.portfolio.Portfolio;
import org.cryptotrader.api.library.entity.portfolio.PortfolioAsset;
import org.cryptotrader.api.library.entity.portfolio.PortfolioAssetHistory;
import org.cryptotrader.api.library.entity.portfolio.PortfolioHistory;
import org.cryptotrader.api.library.entity.user.ProductUser;

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
    @GetMapping("/get")
    public ResponseEntity<Portfolio> getPortfolio() {
        this.portfolio = this.portfolioService.getPortfolioByUserId(this.currentUser.getId());
        return ResponseEntity.ok(this.portfolio);
    }
    //------------------------Add-Portfolio-Asset-----------------------------
    @PostMapping("/add")
    public ResponseEntity<String> addPortfolioAsset(@RequestBody PortfolioAssetRequest portfolioAssetRequest) {
        this.portfolioService.addAssetToPortfolio(this.portfolio, portfolioAssetRequest);
        return ResponseEntity.ok("Asset added to portfolio");
    }
    //-------------------------Is-Empty-Portfolio-----------------------------
    @GetMapping("/empty")
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
    @GetMapping("/history/get")
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
        // TODO: Replace with a DTO.
        List<PortfolioHistory> portfolioHistory = this.portfolioService.getPortfolioHistory(portfolio);
        if (portfolioHistory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(portfolioHistory);
    }
    @GetMapping("/history/get/asset")
    public ResponseEntity<List<PortfolioAssetHistory>> getPortfolioAssetHistory(HttpSession session) {
        ProductUser sessionUser = (ProductUser) session.getAttribute("user");
        this.currentUser = sessionUser;
        this.portfolio = this.portfolioService.getPortfolioByUserId(this.currentUser.getId());
        List<PortfolioAssetHistory> portfolioAssetHistory = this.portfolioService.getPortfolioAssetHistory(this.portfolio);
        return ResponseEntity.ok(portfolioAssetHistory);
    }
    @GetMapping("/history/get/asset/{currencyName}")
    public ResponseEntity<List<PortfolioAssetHistory>> getPortfolioAssetHistoryByCurrency(@PathVariable String currencyName) {
        return null;
    }
    //------------------------Get-Portfolio-Profit----------------------------
    @GetMapping("/history/profit")
    public ResponseEntity<AssetValueResponse> getPortfolioProfit() {
        this.portfolio = this.portfolioService.getPortfolioByUserId(this.currentUser.getId());
        double profit = this.portfolioService.getPortfolioProfit(this.portfolio);
        AssetValueResponse profitResponse = new AssetValueResponse(profit);
        return ResponseEntity.ok(profitResponse);
    }
    //------------------Get-Portfolio-Profit-By-Currency----------------------
    @GetMapping("/history/profit/{currencyName}")
    public ResponseEntity<AssetValueResponse> getPortfolioProfitByCurrency(@PathVariable String currencyName) {
        this.portfolio = this.portfolioService.getPortfolioByUserId(this.currentUser.getId());
        PortfolioAsset portfolioAsset = this.portfolioService.getPortfolioAssetByCurrencyName(this.portfolio, currencyName);
        double profit = this.portfolioService.getPortfolioAssetProfit(portfolioAsset);
        AssetValueResponse profitResponse = new AssetValueResponse(profit);
        return ResponseEntity.ok(profitResponse);
    }
}
