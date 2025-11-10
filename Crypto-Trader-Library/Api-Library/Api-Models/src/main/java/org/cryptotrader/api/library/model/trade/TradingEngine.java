package org.cryptotrader.api.library.model.trade;

import org.cryptotrader.api.library.entity.portfolio.PortfolioAsset;

public interface TradingEngine {
    boolean trade();
    boolean trade(double currentPrice);
    void sell(double currentPrice);
    void buy(double currentPrice);
    PortfolioAsset getAsset();
}