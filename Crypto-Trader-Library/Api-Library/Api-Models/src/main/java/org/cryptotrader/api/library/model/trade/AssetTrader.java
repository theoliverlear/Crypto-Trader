package org.cryptotrader.api.library.model.trade;
//=================================-Imports-==================================
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.api.library.entity.portfolio.PortfolioAsset;

@Getter
@Setter
@Slf4j
public class AssetTrader implements TradingEngine {
    //============================-Variables-=================================
    protected PortfolioAsset asset;
    //===========================-Constructors-===============================
    public AssetTrader(PortfolioAsset asset) {
        this.asset = asset;
    }
    //=============================-Methods-==================================

    //-------------------------------Trade------------------------------------
    @Override
    public boolean trade() {
        double currentPrice = this.asset.getCurrency().getUpdatedValue();
        return this.trade(currentPrice);
    }
    //-------------------------------Trade------------------------------------
    @Override
    public boolean trade(double currentPrice) {
        double targetPrice = this.asset.getTargetPrice();
        if (currentPrice > targetPrice) {
            if (this.asset.canSell()) {
                this.asset.setTargetPrice(currentPrice);
                this.sell(currentPrice);
                return true;
            }
        } else if (currentPrice < targetPrice) {
            if (this.asset.canBuy()) {
                this.asset.setTargetPrice(currentPrice);
                this.buy(currentPrice);
                return true;
            }
        }
        return false;
    }
    //--------------------------------Sell------------------------------------
    @Override
    public void sell(double currentPrice) {
        double valueInDollars = this.asset.getShares() * currentPrice;
        double walletDollars = this.asset.getAssetWalletDollars() + valueInDollars;
        this.asset.setAssetWalletDollars(walletDollars);
        this.asset.updateValues();
        log.info("Selling {} shares of {} for {} dollars.",
                          this.asset.getShares(),
                          this.asset.getCurrency().getName(),
                          walletDollars);
        this.asset.setShares(0);
        this.asset.updateValues();
    }
    //---------------------------------Buy------------------------------------
    @Override
    public void buy(double currentPrice) {
        double shares = this.asset.getAssetWalletDollars() / currentPrice;
        this.asset.setShares(shares);
        this.asset.updateValues();
        log.info("Buying {} shares of {} for {} dollars.",
                          shares,
                          this.asset.getCurrency().getName(),
                          this.asset.getAssetWalletDollars());
        this.asset.setAssetWalletDollars(0);
        this.asset.updateValues();
    }
}
