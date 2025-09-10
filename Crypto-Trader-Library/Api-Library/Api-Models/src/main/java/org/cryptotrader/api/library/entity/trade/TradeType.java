package org.cryptotrader.api.library.entity.trade;

public enum TradeType {
    BUY("Buy"),
    SELL("Sell");
    
    private final String name;
    
    private TradeType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}