package org.theoliverlear.entity.portfolio.builder;

import org.theoliverlear.entity.portfolio.Portfolio;
import org.theoliverlear.entity.portfolio.PortfolioAsset;
import org.theoliverlear.entity.portfolio.PortfolioHistory;
import org.theoliverlear.entity.portfolio.builder.models.AbstractPortfolio;
import org.theoliverlear.entity.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PortfolioBuilder extends AbstractPortfolio {
    private User user;
    private double dollarBalance;
    private double shareBalance;
    private double totalWorth;
    private LocalDateTime lastUpdated;
    private List<PortfolioAsset> assets;
    private List<PortfolioHistory> portfolioHistory;

    public PortfolioBuilder() {
        this.dollarBalance = 0;
        this.shareBalance = 0;
        this.totalWorth = 0;
        this.user = new User();
        this.assets = new ArrayList<>();
        this.portfolioHistory = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }

    @Override
    public AbstractPortfolio user(User user) {
        this.user = user;
        return this;
    }

    @Override
    public AbstractPortfolio dollarBalance(double dollarBalance) {
        this.dollarBalance = dollarBalance;
        return this;
    }

    @Override
    public AbstractPortfolio shareBalance(double shareBalance) {
        this.shareBalance = shareBalance;
        return this;
    }

    @Override
    public AbstractPortfolio totalWorth(double totalWorth) {
        this.totalWorth = totalWorth;
        return this;
    }

    @Override
    public AbstractPortfolio lastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    @Override
    public AbstractPortfolio assets(List<PortfolioAsset> assets) {
        this.assets = assets;
        this.dollarBalance = Portfolio.getTotalDollarValue(assets);
        this.shareBalance = Portfolio.getTotalShareValue(assets);
        this.totalWorth = this.dollarBalance + this.shareBalance;
        this.lastUpdated = LocalDateTime.now();
        return this;
    }

    @Override
    public AbstractPortfolio portfolioHistory(List<PortfolioHistory> portfolioHistory) {
        this.portfolioHistory = portfolioHistory;
        return this;
    }

    @Override
    public Portfolio build() {
        return new Portfolio(this.user,
                             this.dollarBalance,
                             this.shareBalance,
                             this.totalWorth,
                             this.lastUpdated,
                             this.assets,
                             this.portfolioHistory);
    }
}
