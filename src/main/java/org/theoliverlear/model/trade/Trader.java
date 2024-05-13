package org.theoliverlear.model.trade;
//=================================-Imports-==================================
import lombok.Getter;
import lombok.Setter;
import org.theoliverlear.entity.portfolio.Portfolio;
import org.theoliverlear.entity.portfolio.PortfolioAsset;

import java.util.ArrayList;
@Getter
@Setter
public class Trader {
    //============================-Variables-=================================
    // TODO: A trader may take a full portfolio or a single asset. It will
    //       trade based on target prices and a strategy.
    Portfolio portfolio;
    ArrayList<AssetTrader> assetTraders;
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
