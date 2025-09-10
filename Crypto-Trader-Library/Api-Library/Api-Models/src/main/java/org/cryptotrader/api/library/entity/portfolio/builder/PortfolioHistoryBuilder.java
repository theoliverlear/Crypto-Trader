package org.cryptotrader.api.library.entity.portfolio.builder;

import org.cryptotrader.api.library.entity.portfolio.Portfolio;
import org.cryptotrader.api.library.entity.portfolio.PortfolioHistory;
import org.cryptotrader.api.library.entity.portfolio.builder.models.AbstractPortfolioHistory;

import java.time.LocalDateTime;

public class PortfolioHistoryBuilder extends AbstractPortfolioHistory {
    private Portfolio portfolio;
    private double dollarBalance;
    private double shareBalance;
    private double totalWorth;
    private double valueChange;
    private boolean tradeOccurred;
    private LocalDateTime lastUpdated;

    public PortfolioHistoryBuilder() {
        this.dollarBalance = 0;
        this.shareBalance = 0;
        this.totalWorth = 0;
        this.lastUpdated = LocalDateTime.now();
    }

    @Override
    public AbstractPortfolioHistory portfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
        this.dollarBalance = portfolio.getDollarBalance();
        this.shareBalance = portfolio.getShareBalance();
        this.totalWorth = portfolio.getTotalWorth();
        this.lastUpdated = LocalDateTime.now();
        return this;
    }

    @Override
    public AbstractPortfolioHistory dollarBalance(double dollarBalance) {
        this.dollarBalance = dollarBalance;
        return this;
    }

    @Override
    public AbstractPortfolioHistory shareBalance(double shareBalance) {
        this.shareBalance = shareBalance;
        return this;
    }

    @Override
    public AbstractPortfolioHistory totalWorth(double totalWorth) {
        this.totalWorth = totalWorth;
        return this;
    }

    @Override
    public AbstractPortfolioHistory valueChange(double valueChange) {
        this.valueChange = valueChange;
        return this;
    }

    @Override
    public AbstractPortfolioHistory tradeOccurred(boolean tradeOccurred) {
        this.tradeOccurred = tradeOccurred;
        return this;
    }

    @Override
    public AbstractPortfolioHistory lastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    @Override
    public PortfolioHistory build() {
        return new PortfolioHistory(this.portfolio,
                                    this.dollarBalance,
                                    this.shareBalance,
                                    this.totalWorth,
                                    this.valueChange,
                                    this.tradeOccurred,
                                    this.lastUpdated);
    }
}
