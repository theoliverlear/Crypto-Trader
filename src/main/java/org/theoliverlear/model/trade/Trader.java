package org.theoliverlear.model.trade;

import org.theoliverlear.entity.Portfolio;
import org.theoliverlear.entity.PortfolioAsset;

import java.util.ArrayList;
public class Trader {
    // TODO: A trader may take a full portfolio or a single asset. It will
    //       trade based on target prices and a strategy.
    Portfolio portfolio;
    ArrayList<AssetTrader> assetTraders;
    public Trader(Portfolio portfolio) {
        this.portfolio = portfolio;
        this.assetTraders = new ArrayList<>();
        this.initializeAssetTraders();
    }
    public void initializeAssetTraders() {
        for (PortfolioAsset asset : this.portfolio.getAssets()) {
            this.assetTraders.add(new AssetTrader(asset));
        }
    }
    public void tradeAllAssets() {
        for (AssetTrader assetTrader : this.assetTraders) {
            assetTrader.trade();
        }
    }
    public Portfolio getPortfolio() {
        return this.portfolio;
    }
    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
}
