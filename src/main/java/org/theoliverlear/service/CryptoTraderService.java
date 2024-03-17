package org.theoliverlear.service;
//=================================-Imports-==================================
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.theoliverlear.entity.Portfolio;
import org.theoliverlear.entity.User;
import org.theoliverlear.model.trade.Trader;
import org.theoliverlear.repository.PortfolioRepository;
import org.theoliverlear.repository.UserRepository;

@Service
public class CryptoTraderService {
    //============================-Variables-=================================
    UserRepository userRepository;
    PortfolioRepository portfolioRepository;
    //===========================-Constructors-===============================
    @Autowired
    public CryptoTraderService(UserRepository userRepository,
                               PortfolioRepository portfolioRepository) {
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
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
    public void startTrader(User user) {
        Portfolio portfolio = this.getPortfolio(user.getId());
        Trader trader = new Trader(portfolio);
        trader.tradeAllAssets();
    }
}
