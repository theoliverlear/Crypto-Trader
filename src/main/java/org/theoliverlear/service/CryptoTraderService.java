package org.theoliverlear.service;
//=================================-Imports-==================================
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.theoliverlear.CryptoTrader;
import org.theoliverlear.entity.Portfolio;
import org.theoliverlear.entity.User;
import org.theoliverlear.model.trade.Trader;
import org.theoliverlear.repository.PortfolioRepository;
import org.theoliverlear.repository.UserRepository;

import java.util.ArrayList;

@Service
public class CryptoTraderService {
    //============================-Variables-=================================
    UserRepository userRepository;
    PortfolioRepository portfolioRepository;
    CryptoTrader cryptoTrader;
    //===========================-Constructors-===============================
    @Autowired
    public CryptoTraderService(UserRepository userRepository,
                               PortfolioRepository portfolioRepository) {
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
        this.cryptoTrader = new CryptoTrader();
        this.initializeCryptoTrader();
        this.cryptoTrader.startTraders();
    }

    public void initializeCryptoTrader() {
        ArrayList<Portfolio> portfolios = this.getPortfolios();
        ArrayList<Trader> traders = new ArrayList<>();
        for (Portfolio portfolio : portfolios) {
            traders.add(new Trader(portfolio));
        }
        this.cryptoTrader.addTraders(traders);
    }
    //=============================-Methods-==================================
    @Transactional
    public void saveUser(User user) {
        this.userRepository.save(user);
    }
    @Transactional
    public User getUser(String username) {
        return this.userRepository.getUserByUsername(username);
    }
    @Transactional
    public Portfolio getPortfolio(Long userId) {
        return this.portfolioRepository.findPortfolioByUserId(userId);
    }
    @Transactional
    public ArrayList<Portfolio> getPortfolios() {
        return (ArrayList<Portfolio>) this.portfolioRepository.findAll();
    }
    @Transactional
    public ArrayList<User> getUsers() {
        return (ArrayList<User>) this.userRepository.findAll();
    }
    public void startTrader(User user) {
        Portfolio portfolio = this.getPortfolio(user.getId());
        Trader trader = new Trader(portfolio);
        trader.tradeAllAssets();
    }
}
