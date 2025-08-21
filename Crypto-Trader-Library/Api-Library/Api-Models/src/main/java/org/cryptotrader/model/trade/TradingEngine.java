package org.cryptotrader.model.trade;

public interface TradingEngine {
    boolean trade();
    boolean trade(double currentPrice);
    void sell(double currentPrice);
    void buy(double currentPrice);
}