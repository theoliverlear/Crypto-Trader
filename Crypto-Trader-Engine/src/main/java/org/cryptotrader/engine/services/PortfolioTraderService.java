package org.cryptotrader.engine.services;

import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.api.library.entity.portfolio.PortfolioAsset;
import org.cryptotrader.api.library.entity.portfolio.PortfolioAssetHistory;
import org.cryptotrader.api.library.entity.portfolio.PortfolioHistory;
import org.cryptotrader.api.library.model.trade.TradingEngine;
import org.cryptotrader.api.library.services.PortfolioService;
import org.cryptotrader.api.library.entity.portfolio.Portfolio;
import org.cryptotrader.api.library.model.trade.CryptoTrader;
import org.cryptotrader.api.library.model.trade.Trader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@ConditionalOnProperty(name = "cryptotrader.engine.trading.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class PortfolioTraderService {
    private final PortfolioService portfolioService;
    private List<Portfolio> allUsersPortfolios;
    private CryptoTrader cryptoTrader;
    
    @Autowired
    public PortfolioTraderService(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
        this.cryptoTrader = new CryptoTrader();
        this.allUsersPortfolios = this.portfolioService.getAllPortfolios();
        if (this.allUsersPortfolios == null) {
            this.allUsersPortfolios = new ArrayList<>();
        }
        for (Portfolio portfolio : this.allUsersPortfolios) {
            System.out.println(portfolio);
            this.cryptoTrader.addTrader(new Trader(portfolio));
        }
    }

    //--------------------------Trade-Portfolios------------------------------
    // TODO: Different tiers get different intervals.
    @Scheduled(fixedRate = 5000)
    public synchronized void tradePortfolios() {
        this.cryptoTrader.getTraders().clear();
        this.allUsersPortfolios = this.portfolioService.getAllPortfolios();
        this.cryptoTrader.addAllPortfolios(this.allUsersPortfolios);
        if (!this.cryptoTrader.isEmpty()) {
            this.triggerAllTraders();
            log.info("Traders found. Trades are being be made.");
        } else {
            log.info("No traders found. No trades will be made.");
        }
    }
    public void triggerAllTraders() {
        for (Trader trader : this.cryptoTrader.getTraders()) {
            Portfolio previousPortfolio = Portfolio.from(trader.getPortfolio());
            System.out.println(previousPortfolio);
            for (TradingEngine assetTrader : trader.getAssetTraders()) {
                PortfolioAsset previousAsset = PortfolioAsset.from(assetTrader.getAsset());
                boolean tradeOccurred = assetTrader.trade();
                assetTrader.getAsset().updateValues();
                trader.getPortfolio().updateValues();
                if (!previousAsset.equals(assetTrader.getAsset())) {
                    PortfolioAssetHistory portfolioAssetHistory = new PortfolioAssetHistory(assetTrader.getAsset(), tradeOccurred);
                    PortfolioAssetHistory previousPortfolioAssetHistory = this.portfolioService.getLatestPortfolioAssetHistory(assetTrader.getAsset());
                    this.portfolioService.setPortfolioValueChange(previousPortfolioAssetHistory, portfolioAssetHistory);
                    assetTrader.getAsset().addPortfolioAssetHistory(portfolioAssetHistory);
                    PortfolioHistory previousPortfolioHistory = this.portfolioService.getLatestPortfolioHistory(trader.getPortfolio());
                    PortfolioHistory portfolioHistory = new PortfolioHistory(trader.getPortfolio(), tradeOccurred);
                    if (previousPortfolioHistory != null) {
                        portfolioHistory.calculateValueChange(previousPortfolioHistory);
                    } else {
                        portfolioHistory.setValueChange(0);
                    }
                    trader.getPortfolio().addPortfolioHistory(portfolioHistory);
                    this.portfolioService.savePortfolioAsset(assetTrader.getAsset());
                    this.portfolioService.savePortfolio(trader.getPortfolio());
                    this.portfolioService.savePortfolioAssetHistory(portfolioAssetHistory);
                    this.portfolioService.savePortfolioHistory(portfolioHistory);
                }
            }
        }
    }

    //----------------------Add-Portfolio-To-Traders--------------------------
    public void addPortfolioToTraders(Portfolio portfolio) {
        this.cryptoTrader.addTrader(new Trader(portfolio));
    }
}
