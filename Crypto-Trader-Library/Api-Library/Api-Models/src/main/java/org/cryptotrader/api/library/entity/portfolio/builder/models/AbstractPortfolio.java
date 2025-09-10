package org.cryptotrader.api.library.entity.portfolio.builder.models;

import org.cryptotrader.api.library.entity.portfolio.Portfolio;
import org.cryptotrader.api.library.entity.portfolio.PortfolioAsset;
import org.cryptotrader.api.library.entity.portfolio.PortfolioHistory;
import org.cryptotrader.api.library.entity.user.ProductUser;
import org.cryptotrader.api.library.model.BuilderFactory;

import java.time.LocalDateTime;
import java.util.List;

public abstract class AbstractPortfolio implements BuilderFactory<Portfolio> {
    public abstract AbstractPortfolio user(ProductUser user);
    public abstract AbstractPortfolio dollarBalance(double dollarBalance);
    public abstract AbstractPortfolio shareBalance(double shareBalance);
    public abstract AbstractPortfolio totalWorth(double totalWorth);
    public abstract AbstractPortfolio lastUpdated(LocalDateTime lastUpdated);
    public abstract AbstractPortfolio assets(List<PortfolioAsset> assets);
    public abstract AbstractPortfolio portfolioHistory(List<PortfolioHistory> portfolioHistory);
}
