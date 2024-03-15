package org.theoliverlear.model;

import org.theoliverlear.entity.PortfolioAsset;

public class AssetTrader {
    PortfolioAsset asset;
    public AssetTrader(PortfolioAsset asset) {
        this.asset = asset;
    }
    public void trade() {
        double currentPrice = this.asset.getCurrency().getUpdatedValue();
        double targetPrice = this.asset.getTargetPrice();
        if (currentPrice > targetPrice) {
            if (this.asset.getShares() > 0) {
                this.sell();
            }
        } else if (currentPrice < targetPrice) {
            if (this.asset.getAssetWalletDollars() > 0) {
                this.buy();
            }
        }
    }
    public void sell() {

        double valueInDollars = this.asset.getSharesValueInDollars();
        this.asset.setShares(0);
        double walletDollars = this.asset.getAssetWalletDollars() + valueInDollars;
        this.asset.setAssetWalletDollars(walletDollars);
        this.asset.updateValues();
        this.asset.setTargetPrice(this.asset.getCurrency().getValue());
        System.out.printf("Selling %f shares of %s for %f dollars.%n",
                          this.asset.getShares(),
                          this.asset.getCurrency().getName(),
                          walletDollars);
    }
    public void buy() {
        double shares = this.asset.getAssetWalletDollars() /
                        this.asset.getCurrency().getValue();
        this.asset.setShares(shares);
        this.asset.setAssetWalletDollars(0);
        this.asset.updateValues();
        this.asset.setTargetPrice(this.asset.getCurrency().getValue());
        System.out.printf("Buying %f shares of %s for %f dollars.%n",
                          shares,
                          this.asset.getCurrency().getName(),
                          this.asset.getAssetWalletDollars());
    }
}
