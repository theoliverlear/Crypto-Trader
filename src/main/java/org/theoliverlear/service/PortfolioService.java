package org.theoliverlear.service;
//=================================-Imports-==================================
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theoliverlear.model.trade.AssetTrader;
import org.theoliverlear.model.trade.CryptoTrader;
import org.theoliverlear.comm.request.PortfolioAssetRequest;
import org.theoliverlear.entity.currency.Currency;
import org.theoliverlear.entity.portfolio.Portfolio;
import org.theoliverlear.entity.portfolio.PortfolioAsset;
import org.theoliverlear.model.trade.Trader;
import org.theoliverlear.repository.PortfolioAssetRepository;
import org.theoliverlear.repository.PortfolioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PortfolioService {
    //============================-Variables-=================================
    List<Portfolio> allUsersPortfolios;
    PortfolioRepository portfolioRepository;
    PortfolioAssetRepository portfolioAssetRepository;
    CryptoTrader cryptoTrader;
    private CurrencyService currencyService;
    //===========================-Constructors-===============================
    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository,
                            PortfolioAssetRepository portfolioAssetRepository,
                            CurrencyService currencyService) {
        this.cryptoTrader = new CryptoTrader();
        this.currencyService = currencyService;
        this.portfolioRepository = portfolioRepository;
        this.portfolioAssetRepository = portfolioAssetRepository;
        this.allUsersPortfolios = new ArrayList<>();
        this.allUsersPortfolios = this.getAllPortfolios();
        if (this.allUsersPortfolios != null) {
            for (Portfolio portfolio : this.allUsersPortfolios) {
                System.out.println(portfolio);
                this.cryptoTrader.addTrader(new Trader(portfolio));
            }
        }
    }
    //============================-Methods-===================================

    //--------------------------Trade-Portfolios------------------------------
    @Async("taskExecutor")
    @Scheduled(fixedRate = 1000)
    public void tradePortfolios() {
        System.out.println("Trading");
        this.cryptoTrader.getTraders().clear();
        this.allUsersPortfolios = this.getAllPortfolios();
        this.cryptoTrader.addAllPortfolios(this.allUsersPortfolios);
        if (!this.cryptoTrader.isEmpty()) {
            for (Trader trader : this.cryptoTrader.getTraders()) {
                Portfolio previousPortfolio = Portfolio.from(trader.getPortfolio());
                System.out.println(previousPortfolio);
                for (AssetTrader assetTrader : trader.getAssetTraders()) {
                    PortfolioAsset previousAsset = PortfolioAsset.from(assetTrader.getAsset());
                    assetTrader.trade();
                    assetTrader.getAsset().updateValues();
                    trader.getPortfolio().updateValues();
                    if (!previousAsset.equals(assetTrader.getAsset())) {
                        this.savePortfolioAsset(assetTrader.getAsset());
                        this.savePortfolio(trader.getPortfolio());
                    }
                }
            }
        } else {
            System.out.println("No traders");
        }
    }
    //----------------------Add-Portfolio-To-Traders--------------------------
    public void addPortfolioToTraders(Portfolio portfolio) {
        this.cryptoTrader.addTrader(new Trader(portfolio));
    }
    //---------------------------Save-Portfolio-------------------------------
    public void savePortfolio(Portfolio portfolio) {
        this.portfolioRepository.save(portfolio);
    }
    //------------------------Save-Portfolio-Asset----------------------------
    public void savePortfolioAsset(PortfolioAsset portfolioAsset) {
        this.portfolioAssetRepository.save(portfolioAsset);
    }
    //----------------------Get-Portfolio-By-User-Id--------------------------
    public Portfolio getPortfolioByUserId(Long userId) {
        return this.portfolioRepository.findPortfolioByUserId(userId);
    }
    //-------------------------Get-All-Portfolios-----------------------------
    public List<Portfolio> getAllPortfolios() {
        return this.portfolioRepository.findAll();
    }
    //-----------------------Add-Asset-To-Portfolio---------------------------
    public void addAssetToPortfolio(Portfolio portfolio, PortfolioAssetRequest portfolioAssetRequest) {
        Currency requestCurrency = this.currencyService.getCurrencyByName(portfolioAssetRequest.getCurrencyName());
        PortfolioAsset portfolioAsset = new PortfolioAsset(portfolio, requestCurrency, portfolioAssetRequest.getShares(), portfolioAssetRequest.getWalletDollars());
        this.savePortfolio(portfolio);
        this.savePortfolioAsset(portfolioAsset);
    }
}
