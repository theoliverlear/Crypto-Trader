package org.cryptotrader.model.trade;
//=================================-Imports-==================================
import lombok.Getter;
import lombok.Setter;
import org.cryptotrader.entity.portfolio.Portfolio;
import org.cryptotrader.entity.portfolio.PortfolioAsset;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Trader {
    //============================-Variables-=================================
    // TODO: Allow for trades based on target prices and a strategy.
    private Portfolio portfolio;
    private List<AssetTrader> assetTraders;
    //===========================-Constructors-===============================
    public Trader(Portfolio portfolio) {
        this.portfolio = portfolio;
        this.assetTraders = new ArrayList<>();
        this.initializeAssetTraders();
    }
    //============================-Methods-===================================

    //----------------------Initialize-Asset-Traders--------------------------
    public void initializeAssetTraders() {
        for (PortfolioAsset asset : this.portfolio.getAssets()) {
            this.assetTraders.add(new AssetTrader(asset));
        }
    }
    //--------------------------Trade-All-Assets------------------------------
    public void tradeAllAssets() {
        for (AssetTrader assetTrader : this.assetTraders) {
            assetTrader.trade();
        }
    }
}
