package org.theoliverlear.model.trade;

import org.theoliverlear.entity.Portfolio;
import org.theoliverlear.entity.PortfolioAsset;

public class Trader {
    // TODO: A trader may take a full portfolio or a single asset. It will
    //       trade based on target prices and a strategy.
    Portfolio portfolio;
    public Trader(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
    public void tradeAllAssets() {
        for (PortfolioAsset asset : this.portfolio.getAssets()) {
            AssetTrader assetTrader = new AssetTrader(asset);
            assetTrader.trade();
        }
    }
}
