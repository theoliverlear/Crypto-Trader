package org.cryptotrader.api.controller;
//=================================-Imports-==================================
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.cryptotrader.api.library.communication.response.OperationSuccessfulResponse;
import org.cryptotrader.api.library.services.AuthContextService;
import org.cryptotrader.api.library.services.AuthContextServiceKt;
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
    private final PortfolioService portfolioService;
    private final AuthContextService authContextService;
    //===========================-Constructors-===============================
    @Autowired
    public PortfolioController(AuthContextService authContextService,
                               PortfolioService portfolioService) {
        this.authContextService = authContextService;
        this.portfolioService = portfolioService;
    }
    //=============================-Methods-==================================

    // TODO: Replace all entities with a DTO.
    //---------------------------Get-Portfolio--------------------------------
    @GetMapping("/get")
    public ResponseEntity<Portfolio> getPortfolio() {
        boolean isAuthorized = this.authContextService.isAuthenticated();
        if (!isAuthorized) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ProductUser currentUser = this.authContextService.getAuthenticatedProductUser();
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Portfolio portfolio = this.portfolioService.getPortfolioByUserId(currentUser.getId());
        return ResponseEntity.ok(portfolio);
    }
    //------------------------Add-Portfolio-Asset-----------------------------
    @PostMapping("/add")
    public ResponseEntity<OperationSuccessfulResponse> addPortfolioAsset(@RequestBody PortfolioAssetRequest portfolioAssetRequest) {
        if (portfolioAssetRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        boolean isAuthorized = this.authContextService.isAuthenticated();
        if (!isAuthorized) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ProductUser currentUser = this.authContextService.getAuthenticatedProductUser();
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Portfolio portfolio = this.portfolioService.getPortfolioByUserId(currentUser.getId());
        this.portfolioService.addAssetToPortfolio(portfolio, portfolioAssetRequest);
        return ResponseEntity.ok(new OperationSuccessfulResponse(true));
    }
    //-------------------------Is-Empty-Portfolio-----------------------------
    @GetMapping("/empty")
    public ResponseEntity<HasPortfolioResponse> emptyPortfolio() {
        boolean isAuthorized = this.authContextService.isAuthenticated();
        if (!isAuthorized) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ProductUser currentUser = this.authContextService.getAuthenticatedProductUser();
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Portfolio portfolio = this.portfolioService.getPortfolioByUserId(currentUser.getId());
        if (portfolio == null) {
            return ResponseEntity.ok(new HasPortfolioResponse(false));
        } else {
            return ResponseEntity.ok(new HasPortfolioResponse(portfolio.isEmpty()));
        }
    }
    //------------------------Get-Portfolio-History---------------------------
    @GetMapping("/history/get")
    public ResponseEntity<List<PortfolioHistory>> getPortfolioHistory() {
        boolean isAuthorized = this.authContextService.isAuthenticated();
        if (!isAuthorized) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ProductUser currentUser = this.authContextService.getAuthenticatedProductUser();
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Portfolio portfolio = this.portfolioService.getPortfolioByUserId(currentUser.getId());
        // TODO: Replace with a DTO.
        List<PortfolioHistory> portfolioHistory = this.portfolioService.getPortfolioHistory(portfolio);
        if (portfolioHistory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(portfolioHistory);
    }
    @GetMapping("/history/get/asset")
    public ResponseEntity<List<PortfolioAssetHistory>> getPortfolioAssetHistory() {
        boolean isAuthorized = this.authContextService.isAuthenticated();
        if (!isAuthorized) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ProductUser currentUser = this.authContextService.getAuthenticatedProductUser();
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Portfolio portfolio = this.portfolioService.getPortfolioByUserId(currentUser.getId());
        List<PortfolioAssetHistory> portfolioAssetHistory = this.portfolioService.getPortfolioAssetHistory(portfolio);
        return ResponseEntity.ok(portfolioAssetHistory);
    }
    @GetMapping("/history/get/asset/{currencyName}")
    public ResponseEntity<List<PortfolioAssetHistory>> getPortfolioAssetHistoryByCurrency(@PathVariable String currencyName) {
        return null;
    }
    //------------------------Get-Portfolio-Profit----------------------------
    @GetMapping("/history/profit")
    public ResponseEntity<AssetValueResponse> getPortfolioProfit() {
        boolean isAuthorized = this.authContextService.isAuthenticated();
        if (!isAuthorized) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ProductUser currentUser = this.authContextService.getAuthenticatedProductUser();
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Portfolio portfolio = this.portfolioService.getPortfolioByUserId(currentUser.getId());
        double profit = this.portfolioService.getPortfolioProfit(portfolio);
        AssetValueResponse profitResponse = new AssetValueResponse(profit);
        return ResponseEntity.ok(profitResponse);
    }
    //------------------Get-Portfolio-Profit-By-Currency----------------------
    @GetMapping("/history/profit/{currencyName}")
    public ResponseEntity<AssetValueResponse> getPortfolioProfitByCurrency(@PathVariable String currencyName) {
        if (currencyName == null || currencyName.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        boolean isAuthorized = this.authContextService.isAuthenticated();
        if (!isAuthorized) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ProductUser currentUser = this.authContextService.getAuthenticatedProductUser();
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Portfolio portfolio = this.portfolioService.getPortfolioByUserId(currentUser.getId());
        PortfolioAsset portfolioAsset = this.portfolioService.getPortfolioAssetByCurrencyName(portfolio, currencyName);
        double profit = this.portfolioService.getPortfolioAssetProfit(portfolioAsset);
        AssetValueResponse profitResponse = new AssetValueResponse(profit);
        return ResponseEntity.ok(profitResponse);
    }
}
