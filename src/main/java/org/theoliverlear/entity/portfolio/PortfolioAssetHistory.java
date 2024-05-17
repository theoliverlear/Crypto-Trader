package org.theoliverlear.entity.portfolio;
//=================================-Imports-==================================
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.theoliverlear.entity.currency.Currency;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "portfolio_asset_history")
@Entity
public class PortfolioAssetHistory {
    //============================-Variables-=================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    //===========================-Constructors-===============================
    public PortfolioAssetHistory() {
        this.currency = new Currency();
        this.shares = 0;
        this.sharesValueInDollars = 0;
        this.assetWalletDollars = 0;
        this.targetPrice = this.currency.getValue();
        this.lastUpdated = LocalDateTime.now();
    }
    public PortfolioAssetHistory(PortfolioAsset portfolioAsset) {
        this.portfolioAsset = portfolioAsset;
        this.portfolio = portfolioAsset.getPortfolio();
        this.currency = portfolioAsset.getCurrency();
        this.shares = portfolioAsset.getShares();
        this.sharesValueInDollars = portfolioAsset.getSharesValueInDollars();
        this.assetWalletDollars = portfolioAsset.getAssetWalletDollars();
        this.totalValueInDollars = portfolioAsset.getTotalValueInDollars();
        this.targetPrice = portfolioAsset.getTargetPrice();
        this.lastUpdated = LocalDateTime.now();
    }
    public PortfolioAssetHistory(PortfolioAsset portfolioAsset, LocalDateTime lastUpdated) {
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
}
