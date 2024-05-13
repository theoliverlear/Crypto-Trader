package org.theoliverlear.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theoliverlear.CryptoTrader;
import org.theoliverlear.comm.request.PortfolioAssetRequest;
import org.theoliverlear.entity.Currency;
import org.theoliverlear.entity.Portfolio;
import org.theoliverlear.entity.PortfolioAsset;
import org.theoliverlear.entity.User;
import org.theoliverlear.model.trade.Trader;
import org.theoliverlear.repository.PortfolioAssetRepository;
import org.theoliverlear.repository.PortfolioRepository;
import org.theoliverlear.update.SupportedCurrencies;

import java.util.ArrayList;
import java.util.List;

@Service
public class PortfolioService {
    List<Portfolio> allUsersPortfolios;
    PortfolioRepository portfolioRepository;
    PortfolioAssetRepository portfolioAssetRepository;
    CryptoTrader cryptoTrader;
    private CurrencyService currencyService;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository,
                            PortfolioAssetRepository portfolioAssetRepository,
                            CurrencyService currencyService) {
        this.cryptoTrader = new CryptoTrader();
        this.currencyService = currencyService;
        this.portfolioRepository = portfolioRepository;
        this.portfolioAssetRepository = portfolioAssetRepository;
        this.allUsersPortfolios = new ArrayList<>();
        this.allUsersPortfolios = this.getPortfolios();
        if (this.allUsersPortfolios != null) {
            for (Portfolio portfolio : this.allUsersPortfolios) {
                this.cryptoTrader.addTrader(new Trader(portfolio));
            }
        }
    }
    @Async("taskExecutor")
    @Scheduled(fixedRate = 5000)
    public void trade() {
        if (!this.cryptoTrader.isEmpty()) {
            for (Trader trader : this.cryptoTrader.getTraders()) {
                Portfolio previousPortfolio = trader.getPortfolio();
                trader.tradeAllAssets();
                if (!previousPortfolio.equals(trader.getPortfolio())) {
                    this.savePortfolio(trader.getPortfolio());
                }
            }
        }
    }
    public void savePortfolio(Portfolio portfolio) {
        this.portfolioRepository.save(portfolio);
    }
    public void savePortfolioAsset(PortfolioAsset portfolioAsset) {
        this.portfolioAssetRepository.save(portfolioAsset);
    }

    public Portfolio getPortfolio(Long userId) {
        return this.portfolioRepository.findPortfolioByUserId(userId);
    }
    public List<Portfolio> getPortfolios() {
        return this.portfolioRepository.findAll();
    }

    public void addAssetToPortfolio(Portfolio portfolio, PortfolioAssetRequest portfolioAssetRequest) {
        Currency requestCurrency = this.currencyService.getCurrencyByName(portfolioAssetRequest.getCurrencyName());
        PortfolioAsset portfolioAsset = new PortfolioAsset(portfolio, requestCurrency, portfolioAssetRequest.getShares(), portfolioAssetRequest.getWalletDollars());
        portfolio.getAssets().add(portfolioAsset);
        this.savePortfolio(portfolio);
        this.savePortfolioAsset(portfolioAsset);
    }
}
