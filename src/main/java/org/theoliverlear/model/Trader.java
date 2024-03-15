package org.theoliverlear.model;

import org.theoliverlear.entity.Portfolio;

public class Trader {
    // TODO: A trader may take a full portfolio or a single asset. It will
    //       trade based on target prices and a strategy.
    Portfolio portfolio;
    public Trader(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
    public void tradeAllAssets() {

    }
}
