package org.theoliverlear.service;
//=================================-Imports-==================================
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theoliverlear.entity.portfolio.*;
import org.theoliverlear.model.trade.AssetTrader;
import org.theoliverlear.model.trade.CryptoTrader;
import org.theoliverlear.comm.request.PortfolioAssetRequest;
import org.theoliverlear.entity.currency.Currency;
import org.theoliverlear.model.trade.Trader;
import org.theoliverlear.repository.PortfolioAssetHistoryRepository;
import org.theoliverlear.repository.PortfolioAssetRepository;
import org.theoliverlear.repository.PortfolioHistoryRepository;
import org.theoliverlear.repository.PortfolioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PortfolioService {
    //============================-Variables-=================================
    private List<Portfolio> allUsersPortfolios;
    private PortfolioRepository portfolioRepository;
    private PortfolioAssetRepository portfolioAssetRepository;
    private PortfolioHistoryRepository portfolioHistoryRepository;
    private PortfolioAssetHistoryRepository portfolioAssetHistoryRepository;
    private CryptoTrader cryptoTrader;
    private CurrencyService currencyService;
    //===========================-Constructors-===============================
    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository,
                            PortfolioAssetRepository portfolioAssetRepository,
                            PortfolioHistoryRepository portfolioHistoryRepository,
                            PortfolioAssetHistoryRepository portfolioAssetHistoryRepository,
                            CurrencyService currencyService) {
        this.cryptoTrader = new CryptoTrader();
        this.currencyService = currencyService;
        this.portfolioRepository = portfolioRepository;
        this.portfolioAssetRepository = portfolioAssetRepository;
        this.portfolioHistoryRepository = portfolioHistoryRepository;
        this.portfolioAssetHistoryRepository = portfolioAssetHistoryRepository;
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
    @Scheduled(fixedRate = 2000)
    public synchronized void tradePortfolios() {
        System.out.println("Trading");
        this.cryptoTrader.getTraders().clear();
        this.allUsersPortfolios = this.getAllPortfolios();
        this.cryptoTrader.addAllPortfolios(this.allUsersPortfolios);
        if (!this.cryptoTrader.isEmpty()) {
            this.triggerAllTraders();
        } else {
            System.out.println("No traders");
        }
    }
    public void triggerAllTraders() {
        for (Trader trader : this.cryptoTrader.getTraders()) {
            Portfolio previousPortfolio = Portfolio.from(trader.getPortfolio());
            System.out.println(previousPortfolio);
            for (AssetTrader assetTrader : trader.getAssetTraders()) {
                PortfolioAsset previousAsset = PortfolioAsset.from(assetTrader.getAsset());
                boolean tradeOccurred = assetTrader.trade();
                assetTrader.getAsset().updateValues();
                trader.getPortfolio().updateValues();
                if (!previousAsset.equals(assetTrader.getAsset())) {
                    PortfolioAssetHistory portfolioAssetHistory = new PortfolioAssetHistory(assetTrader.getAsset(), tradeOccurred);
                    PortfolioAssetHistory previousPortfolioAssetHistory = this.getLatestPortfolioAssetHistory(assetTrader.getAsset());
                    this.setPortfolioValueChange(previousPortfolioAssetHistory, portfolioAssetHistory);
                    assetTrader.getAsset().addPortfolioAssetHistory(portfolioAssetHistory);
                    PortfolioHistory previousPortfolioHistory = this.getLatestPortfolioHistory(trader.getPortfolio());
                    PortfolioHistory portfolioHistory = new PortfolioHistory(trader.getPortfolio(), tradeOccurred);
                    if (previousPortfolioHistory != null) {
                        portfolioHistory.calculateValueChange(previousPortfolioHistory);
                    } else {
                        portfolioHistory.setValueChange(0);
                    }
                    trader.getPortfolio().addPortfolioHistory(portfolioHistory);
                    this.savePortfolioAsset(assetTrader.getAsset());
                    this.savePortfolio(trader.getPortfolio());
                    this.savePortfolioAssetHistory(portfolioAssetHistory);
                    this.savePortfolioHistory(portfolioHistory);
                }
            }
        }
    }
    private void setPortfolioValueChange(PortfolioAssetHistory previousPortfolioAssetHistory, PortfolioAssetHistory portfolioAssetHistory) {
        if (previousPortfolioAssetHistory != null) {
            portfolioAssetHistory.calculateValueChange(previousPortfolioAssetHistory);
        } else {
            portfolioAssetHistory.setValueChange(0);
        }
    }

    public List<PortfolioAssetHistory> getPortfolioAssetHistory(Portfolio portfolio) {
        return this.portfolioAssetHistoryRepository.findAllByPortfolioId(portfolio.getId());
    }
    public PortfolioAssetHistory getLatestPortfolioAssetHistory(PortfolioAsset portfolioAsset) {
        return this.portfolioAssetHistoryRepository.findFirstByPortfolioAssetIdOrderByLastUpdatedDesc(portfolioAsset.getId());
    }
    public PortfolioHistory getLatestPortfolioHistory(Portfolio portfolio) {
        return this.portfolioHistoryRepository.findFirstByPortfolioIdOrderByLastUpdatedDesc(portfolio.getId());
    }
    //---------------------Save-Portfolio-Asset-History-----------------------
    public void savePortfolioAssetHistory(PortfolioAssetHistory portfolioAssetHistory) {
        this.portfolioAssetHistoryRepository.save(portfolioAssetHistory);
    }
    //----------------------Save-Portfolio-History----------------------------
    public void savePortfolioHistory(PortfolioHistory portfolioHistory) {
        this.portfolioHistoryRepository.save(portfolioHistory);
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
    //-----------------------Get-Portfolio-History----------------------------
    public List<PortfolioHistory> getPortfolioHistory(Portfolio portfolio) {
        return this.portfolioHistoryRepository.findAllByPortfolioId(portfolio.getId());
    }
    //------------------------Get-Portfolio-Profit----------------------------
    public double getPortfolioProfit(Portfolio portfolio) {
        PortfolioHistory initialPortfolioHistory = this.portfolioHistoryRepository.getFirstByPortfolioId(portfolio.getId());
        if (initialPortfolioHistory == null) {
            return 0;
        }
        double profit = portfolio.getTotalWorth() - initialPortfolioHistory.getTotalWorth();
        return profit;
    }
    //---------------------Get-Portfolio-Asset-Profit-------------------------
    public double getPortfolioAssetProfit(PortfolioAsset portfolioAsset) {
        PortfolioAssetHistory initialPortfolioAssetHistory = this.portfolioAssetHistoryRepository.getFirstByPortfolioAssetId(portfolioAsset.getId());
        if (initialPortfolioAssetHistory == null) {
            return 0;
        }
        double profit = portfolioAsset.getTotalValueInDollars() - initialPortfolioAssetHistory.getTotalValueInDollars();
        return profit;
    }
    //----------------Get-Portfolio-Asset-By-Currency-Name--------------------
    public PortfolioAsset getPortfolioAssetByCurrencyName(Portfolio portfolio, String currencyName) {
        return this.portfolioAssetRepository.getPortfolioAssetByPortfolioIdAndCurrencyName(portfolio.getId(), currencyName);
    }
}