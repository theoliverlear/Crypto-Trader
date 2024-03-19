package org.theoliverlear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.theoliverlear.CryptoTrader;
import org.theoliverlear.entity.Portfolio;
import org.theoliverlear.entity.PortfolioAsset;
import org.theoliverlear.model.trade.Trader;
import org.theoliverlear.repository.PortfolioRepository;
import org.theoliverlear.update.PortfolioUpdater;
import org.theoliverlear.update.SupportedCurrencies;

import java.util.ArrayList;

@Service
public class PortfolioService {
    ArrayList<Portfolio> allUsersPortfolios;
    PortfolioRepository portfolioRepository;
    PortfolioUpdater portfolioUpdater;
    CryptoTrader cryptoTrader;
    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
        this.allUsersPortfolios = this.getPortfolios();
        ArrayList<PortfolioAsset> assets = new ArrayList<>();
        assets.add(new PortfolioAsset(SupportedCurrencies.BITCOIN, 1.0, 0));
        this.allUsersPortfolios.add(new Portfolio(1L, assets));
        this.portfolioUpdater = new PortfolioUpdater(this.allUsersPortfolios);
        this.portfolioUpdater.startPortfolioUpdaters();
        this.startPortfolioRepositoryUpdater();
        ArrayList<Trader> traders = new ArrayList<>();
        for (Portfolio portfolio : this.allUsersPortfolios) {
            traders.add(new Trader(portfolio));
        }
        this.cryptoTrader = new CryptoTrader(traders);
        this.cryptoTrader.startTraders();
    }

    public void startPortfolioRepositoryUpdater() {
        Runnable updatePortfolios = () -> {
            ArrayList<Portfolio> updatedPortfolios = this.portfolioUpdater.getPortfolios();
            for (Portfolio portfolio : updatedPortfolios) {
                this.savePortfolio(portfolio);
            }
        };
        Thread portfolioRepositoryUpdater = new Thread(updatePortfolios);
        portfolioRepositoryUpdater.start();
    }
    public void savePortfolio(Portfolio portfolio) {
        this.portfolioRepository.save(portfolio);
    }
    @Transactional
    public Portfolio getPortfolio(Long userId) {
        return this.portfolioRepository.findPortfolioByUserId(userId);
    }
    public ArrayList<Portfolio> getPortfolios() {
        return (ArrayList<Portfolio>) this.portfolioRepository.findAll();
    }

}
