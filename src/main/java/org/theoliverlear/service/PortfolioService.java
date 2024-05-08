package org.theoliverlear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theoliverlear.CryptoTrader;
import org.theoliverlear.comm.request.PortfolioAssetRequest;
import org.theoliverlear.entity.Currency;
import org.theoliverlear.entity.Portfolio;
import org.theoliverlear.entity.PortfolioAsset;
import org.theoliverlear.model.trade.Trader;
import org.theoliverlear.repository.PortfolioRepository;
import org.theoliverlear.update.SupportedCurrencies;

import java.util.ArrayList;

@Service
public class PortfolioService {
    ArrayList<Portfolio> allUsersPortfolios;
    PortfolioRepository portfolioRepository;
    CryptoTrader cryptoTrader;
    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.cryptoTrader = new CryptoTrader();
        this.portfolioRepository = portfolioRepository;
        this.allUsersPortfolios = new ArrayList<>();
        this.allUsersPortfolios = this.getPortfolios();
        if (this.allUsersPortfolios != null) {
            for (Portfolio portfolio : this.allUsersPortfolios) {
                this.cryptoTrader.addTrader(new Trader(portfolio));
            }
        }
    }
    @Async
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
    public Currency getCurrencyFromRequest(PortfolioAssetRequest portfolioAssetRequest) {
        Currency requestCurrency = null;
        for (Currency currency : SupportedCurrencies.SUPPORTED_CURRENCIES) {
            if (currency.getName().equals(portfolioAssetRequest.getCurrencyName())) {
                requestCurrency = currency;
                break;
            }
        }
        return requestCurrency;
    }
    public Portfolio getPortfolio(Long userId) {
        return this.portfolioRepository.findPortfolioByUserId(userId);
    }
    public ArrayList<Portfolio> getPortfolios() {
        return (ArrayList<Portfolio>) this.portfolioRepository.findAll();
    }

    public void addAssetToPortfolio(Portfolio portfolio, PortfolioAssetRequest portfolioAssetRequest) {
        Currency requestCurrency = this.getCurrencyFromRequest(portfolioAssetRequest);
        PortfolioAsset portfolioAsset = new PortfolioAsset(portfolio, requestCurrency, portfolioAssetRequest.getShares(), portfolioAssetRequest.getAssetWalletDollars());
        portfolio.addAsset(portfolioAsset);
        this.savePortfolio(portfolio);
    }
}
