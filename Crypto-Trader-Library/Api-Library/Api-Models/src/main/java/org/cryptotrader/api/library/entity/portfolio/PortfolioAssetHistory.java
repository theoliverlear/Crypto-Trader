package org.cryptotrader.api.library.entity.portfolio;
//=================================-Imports-==================================
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.cryptotrader.universal.library.entity.Identifiable;
import org.cryptotrader.data.library.entity.currency.Currency;
import org.cryptotrader.api.library.entity.vendor.SupportedVendors;
import org.cryptotrader.api.library.entity.vendor.Vendor;

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
    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
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
    private boolean tradeOccurred;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "vendor_name", length = 255)),
            @AttributeOverride(name = "rate", column = @Column(name = "vendor_rate", columnDefinition = "DECIMAL(6, 10)"))
    })
    private Vendor vendor;
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
        this.vendor = SupportedVendors.PAPER_MODE;
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
        this.vendor = portfolioAsset.getVendor();
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
        this.vendor = portfolioAsset.getVendor();
        this.lastUpdated = lastUpdated;
    }
    @Override
    public void calculateValueChange(PortfolioAssetHistory previousHistory) {
        if (previousHistory == null) {
            this.valueChange = 0;
        } else {
            this.valueChange = this.totalValueInDollars - previousHistory.getTotalValueInDollars();
        }
    }
    
    public void calculateShareChange(PortfolioAssetHistory previousHistoryWithShares) {
        if (previousHistoryWithShares == null) {
            this.sharesChange = 0;
        } else {
            double delta = this.shares - previousHistoryWithShares.getShares();
            this.sharesChange = Math.max(delta, 0);
        }
    }
}
