package org.cryptotrader.engine.services;

import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.api.services.PortfolioService;
import org.cryptotrader.entity.portfolio.Portfolio;
import org.cryptotrader.model.trade.CryptoTrader;
import org.cryptotrader.model.trade.Trader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PortfolioTraderService {
    private PortfolioService portfolioService;
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
            // TODO: Reimplement using TradingEngine interface.
//            for (AssetTrader assetTrader : trader.getAssetTraders()) {
//                PortfolioAsset previousAsset = PortfolioAsset.from(assetTrader.getAsset());
//                boolean tradeOccurred = assetTrader.trade();
//                assetTrader.getAsset().updateValues();
//                trader.getPortfolio().updateValues();
//                if (!previousAsset.equals(assetTrader.getAsset())) {
//                    PortfolioAssetHistory portfolioAssetHistory = new PortfolioAssetHistory(assetTrader.getAsset(), tradeOccurred);
//                    PortfolioAssetHistory previousPortfolioAssetHistory = this.getLatestPortfolioAssetHistory(assetTrader.getAsset());
//                    this.setPortfolioValueChange(previousPortfolioAssetHistory, portfolioAssetHistory);
//                    assetTrader.getAsset().addPortfolioAssetHistory(portfolioAssetHistory);
//                    PortfolioHistory previousPortfolioHistory = this.getLatestPortfolioHistory(trader.getPortfolio());
//                    PortfolioHistory portfolioHistory = new PortfolioHistory(trader.getPortfolio(), tradeOccurred);
//                    if (previousPortfolioHistory != null) {
//                        portfolioHistory.calculateValueChange(previousPortfolioHistory);
//                    } else {
//                        portfolioHistory.setValueChange(0);
//                    }
//                    trader.getPortfolio().addPortfolioHistory(portfolioHistory);
//                    this.savePortfolioAsset(assetTrader.getAsset());
//                    this.savePortfolio(trader.getPortfolio());
//                    this.savePortfolioAssetHistory(portfolioAssetHistory);
//                    this.savePortfolioHistory(portfolioHistory);
//                }
//            }
        }
    }

    //----------------------Add-Portfolio-To-Traders--------------------------
    public void addPortfolioToTraders(Portfolio portfolio) {
        this.cryptoTrader.addTrader(new Trader(portfolio));
    }
}
