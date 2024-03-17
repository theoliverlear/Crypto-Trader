package org.theoliverlear.model.trade;
//=================================-Imports-==================================
import org.theoliverlear.entity.PortfolioAsset;

public class AssetTrader {
    //============================-Variables-=================================
    PortfolioAsset asset;
    //===========================-Constructors-===============================
    public AssetTrader(PortfolioAsset asset) {
        this.asset = asset;
    }
    //=============================-Methods-==================================

    //-------------------------------Trade------------------------------------
    public void trade() {
        double currentPrice = this.asset.getCurrency().getUpdatedValue();
        double targetPrice = this.asset.getTargetPrice();
        if (currentPrice > targetPrice) {
            if (this.asset.canSell()) {
                this.sell();
            }
        } else if (currentPrice < targetPrice) {
            if (this.asset.canBuy()) {
                this.buy();
            }
        }
    }
    //--------------------------------Sell------------------------------------
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
    //---------------------------------Buy------------------------------------
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
