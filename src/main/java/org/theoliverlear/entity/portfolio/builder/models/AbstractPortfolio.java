package org.theoliverlear.entity.portfolio.builder.models;

import org.theoliverlear.entity.portfolio.Portfolio;
import org.theoliverlear.entity.portfolio.PortfolioAsset;
import org.theoliverlear.entity.portfolio.PortfolioHistory;
import org.theoliverlear.entity.user.User;
import org.theoliverlear.model.BuilderFactory;

import java.time.LocalDateTime;
import java.util.List;

public abstract class AbstractPortfolio implements BuilderFactory<Portfolio> {
    public abstract AbstractPortfolio user(User user);
    public abstract AbstractPortfolio dollarBalance(double dollarBalance);
    public abstract AbstractPortfolio shareBalance(double shareBalance);
    public abstract AbstractPortfolio totalWorth(double totalWorth);
    public abstract AbstractPortfolio lastUpdated(LocalDateTime lastUpdated);
    public abstract AbstractPortfolio assets(List<PortfolioAsset> assets);
    public abstract AbstractPortfolio portfolioHistory(List<PortfolioHistory> portfolioHistory);
}
