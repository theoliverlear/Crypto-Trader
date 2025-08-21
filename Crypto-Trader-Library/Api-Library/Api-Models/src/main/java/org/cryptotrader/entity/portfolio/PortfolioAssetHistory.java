package org.cryptotrader.entity.portfolio;
//=================================-Imports-==================================
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.cryptotrader.entity.Identifiable;
import org.cryptotrader.entity.currency.Currency;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "portfolio_asset_history")
@Entity
public class PortfolioAssetHistory extends Identifiable implements SequentiallyValuable<PortfolioAssetHistory> {
    //============================-Variables-=================================
    @ManyToOne
    @JoinColumn(name = "portfolio_asset_id", nullable = false)
    private PortfolioAsset portfolioAsset;
    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "currency_code", nullable = false)
    private Currency currency;
    @Column(name = "shares", columnDefinition = "DECIMAL(34, 18)")
    private double shares;
    @Column(name = "shares_value_in_dollars", columnDefinition = "DECIMAL(34, 18)")
    private double sharesValueInDollars;
    @Column(name = "asset_wallet_dollars", columnDefinition = "DECIMAL(34, 18)")
    private double assetWalletDollars;
    @Column(name = "total_value_in_dollars", columnDefinition = "DECIMAL(34, 18)")
    private double totalValueInDollars;
    @Column(name = "target_price", columnDefinition = "DECIMAL(34, 18)")
    private double targetPrice;
    @Column(name = "value_change", columnDefinition = "DECIMAL(34, 18)")
    private double valueChange;
    @Column(name = "share_change", columnDefinition = "DECIMAL(34, 18)")
    private double sharesChange;
    @Column(name = "trade_occurred")
    boolean tradeOccurred;
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    //===========================-Constructors-===============================
    public PortfolioAssetHistory() {
        super();
        this.currency = new Currency();
        this.shares = 0;
        this.sharesValueInDollars = 0;
        this.assetWalletDollars = 0;
        this.targetPrice = this.currency.getValue();
        this.lastUpdated = LocalDateTime.now();
    }
    public PortfolioAssetHistory(PortfolioAsset portfolioAsset, boolean tradeOccurred) {
        super();
        this.portfolioAsset = portfolioAsset;
        this.portfolio = portfolioAsset.getPortfolio();
        this.currency = portfolioAsset.getCurrency();
        this.shares = portfolioAsset.getShares();
        this.sharesValueInDollars = portfolioAsset.getSharesValueInDollars();
        this.assetWalletDollars = portfolioAsset.getAssetWalletDollars();
        this.totalValueInDollars = portfolioAsset.getTotalValueInDollars();
        this.targetPrice = portfolioAsset.getTargetPrice();
        this.tradeOccurred = tradeOccurred;
        this.lastUpdated = LocalDateTime.now();
    }
    public PortfolioAssetHistory(PortfolioAsset portfolioAsset, LocalDateTime lastUpdated) {
        super();
        this.portfolioAsset = portfolioAsset;
        this.portfolio = portfolioAsset.getPortfolio();
        this.currency = portfolioAsset.getCurrency();
        this.shares = portfolioAsset.getShares();
        this.sharesValueInDollars = portfolioAsset.getSharesValueInDollars();
        this.assetWalletDollars = portfolioAsset.getAssetWalletDollars();
        this.totalValueInDollars = portfolioAsset.getTotalValueInDollars();
        this.targetPrice = portfolioAsset.getTargetPrice();
        this.lastUpdated = lastUpdated;
    }
    @Override
    public void calculateValueChange(PortfolioAssetHistory previousHistory) {
        if (previousHistory == null) {
            this.valueChange = 0;
        } else {
            this.sharesChange = this.shares - previousHistory.getShares();
            this.valueChange = this.totalValueInDollars - previousHistory.getTotalValueInDollars();
        }
    }
}
