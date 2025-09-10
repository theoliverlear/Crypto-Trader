package org.cryptotrader.api.library.entity.portfolio.builder.models;

import org.cryptotrader.api.library.entity.portfolio.Portfolio;
import org.cryptotrader.api.library.entity.portfolio.PortfolioHistory;
import org.cryptotrader.api.library.model.BuilderFactory;

import java.time.LocalDateTime;

public abstract class AbstractPortfolioHistory implements BuilderFactory<PortfolioHistory> {
    public abstract AbstractPortfolioHistory portfolio(Portfolio portfolio);
    public abstract AbstractPortfolioHistory dollarBalance(double dollarBalance);
    public abstract AbstractPortfolioHistory shareBalance(double shareBalance);
    public abstract AbstractPortfolioHistory totalWorth(double totalWorth);
    public abstract AbstractPortfolioHistory valueChange(double valueChange);
    public abstract AbstractPortfolioHistory tradeOccurred(boolean tradeOccurred);
    public abstract AbstractPortfolioHistory lastUpdated(LocalDateTime lastUpdated);
}
