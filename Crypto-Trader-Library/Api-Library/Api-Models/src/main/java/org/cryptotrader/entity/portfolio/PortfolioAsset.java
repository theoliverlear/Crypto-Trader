package org.cryptotrader.entity.portfolio;
//=================================-Imports-==================================

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.cryptotrader.entity.Identifiable;
import org.cryptotrader.entity.currency.Currency;
import org.cryptotrader.entity.currency.SupportedCurrencies;
import org.cryptotrader.entity.vendor.SupportedVendors;
import org.cryptotrader.entity.vendor.Vendor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "portfolio_assets")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PortfolioAsset extends Identifiable implements UpdatableValues {
    //============================-Variables-=================================
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
    @OneToMany(mappedBy = "portfolioAsset", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PortfolioAssetHistory> assetHistory;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "vendor_name", length = 255)),
            @AttributeOverride(name = "rate", column = @Column(name = "vendor_rate", columnDefinition = "DECIMAL(6, 10)"))
    })
    private Vendor vendor;
    // TODO: Add buying strategy which may sell the whole asset, only the
    //       profits, or a set amount or percentage of the asset.
    //===========================-Constructors-===============================
    public PortfolioAsset() {
        super();
        this.currency = SupportedCurrencies.BITCOIN;
        this.shares = 0;
        this.sharesValueInDollars = 0;
        this.assetWalletDollars = 0;
        this.targetPrice = this.currency.getValue();
        this.assetHistory = new ArrayList<>();
        this.vendor = SupportedVendors.PAPER_MODE;
        this.lastUpdated = LocalDateTime.now();
    }
    public PortfolioAsset(Portfolio portfolio, Currency currency, double shares, double assetWalletDollars) {
        super();
        this.portfolio = portfolio;
        this.currency = currency;
        this.shares = shares;
        this.assetWalletDollars = assetWalletDollars;
        this.targetPrice = currency.getValue();
        this.portfolio.addAsset(this);
        this.assetHistory = new ArrayList<>();
        this.vendor = SupportedVendors.PAPER_MODE;
        this.lastUpdated = LocalDateTime.now();
    }
    public PortfolioAsset(Currency currency, double shares, double assetWalletDollars) {
        super();
        this.currency = currency;
        this.shares = shares;
        this.assetWalletDollars = assetWalletDollars;
        this.targetPrice = currency.getValue();
        this.assetHistory = new ArrayList<>();
        this.vendor = SupportedVendors.PAPER_MODE;
        this.lastUpdated = LocalDateTime.now();
    }
    //=============================-Methods-==================================

    //--------------------Add-Portfolio-Asset-History-------------------------
    public void addPortfolioAssetHistory(PortfolioAssetHistory portfolioAssetHistory) {
        this.assetHistory.add(portfolioAssetHistory);
    }
    //--------------------Fetch-Total-Value-In-Dollars------------------------
    public void fetchTotalValueInDollars() {
        double sharesValue = this.shares * this.currency.getValue();
        this.totalValueInDollars = sharesValue + this.assetWalletDollars;
    }
    //-------------------Fetch-Shares-Value-In-Dollars------------------------
    public void fetchSharesValueInDollars() {
        this.sharesValueInDollars = this.shares * this.currency.getValue();
    }
    //------------------------------Can-Sell----------------------------------
    public boolean canSell() {
        return this.shares > 0;
    }
    //------------------------------Can-Buy-----------------------------------
    public boolean canBuy() {
        return this.assetWalletDollars > 0;
    }
    //--------------------------------From------------------------------------
    public static PortfolioAsset from(PortfolioAsset portfolioAsset) {
        PortfolioAsset newPortfolioAsset = PortfolioAsset.builder()
                                                         .portfolio(portfolioAsset.getPortfolio())
                                                         .currency(portfolioAsset.getCurrency())
                                                         .shares(portfolioAsset.getShares())
                                                         .sharesValueInDollars(portfolioAsset.getSharesValueInDollars())
                                                         .assetWalletDollars(portfolioAsset.getAssetWalletDollars())
                                                         .totalValueInDollars(portfolioAsset.getTotalValueInDollars())
                                                         .targetPrice(portfolioAsset.getTargetPrice())
                                                         .vendor(portfolioAsset.getVendor())
                                                         .build();
        return newPortfolioAsset;
    }
    //============================-Overrides-=================================

    //---------------------------Update-Values--------------------------------
    @Override
    public void updateValues() {
        this.fetchSharesValueInDollars();
        this.fetchTotalValueInDollars();
        this.lastUpdated = LocalDateTime.now();
    }
    //------------------------------Equals------------------------------------
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof PortfolioAsset portfolioAsset) {
            boolean samePortfolio = this.portfolio.equals(portfolioAsset.portfolio);
            boolean sameCurrency = this.currency.equals(portfolioAsset.currency);
            boolean sameShares = this.shares == portfolioAsset.shares;
            boolean sameSharesValueInDollars = this.sharesValueInDollars == portfolioAsset.sharesValueInDollars;
            boolean sameAssetWalletDollars = this.assetWalletDollars == portfolioAsset.assetWalletDollars;
            boolean sameTotalValueInDollars = this.totalValueInDollars == portfolioAsset.totalValueInDollars;
            boolean sameTargetPrice = this.targetPrice == portfolioAsset.targetPrice;
            if (this.id != null) {
                boolean sameId = this.id.equals(portfolioAsset.id);
                return sameId && samePortfolio && sameCurrency && sameShares
                        && sameSharesValueInDollars && sameAssetWalletDollars
                        && sameTotalValueInDollars && sameTargetPrice;
            }
            return samePortfolio && sameCurrency && sameShares
                    && sameSharesValueInDollars && sameAssetWalletDollars
                    && sameTotalValueInDollars && sameTargetPrice;
        }
        return false;
    }
    //------------------------------Hash-Code---------------------------------
    @Override
    public int hashCode() {
        if (this.id != null) {
            return this.id.hashCode();
        } else {
            return super.hashCode();
        }
    }
    //------------------------------To-String---------------------------------
    @Override
    public String toString() {
        return "%s, %f shares, %f wallet dollars, %f total value in dollars"
                .formatted(this.currency, this.shares,
                           this.assetWalletDollars, this.totalValueInDollars);
    }
}
