package org.theoliverlear.entity;
//=================================-Imports-==================================
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.theoliverlear.update.SupportedCurrencies;

@Getter
@Setter
@Entity
@Table(name = "portfolio_assets")
public class PortfolioAsset {
    //============================-Variables-=================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;
    @ManyToOne(optional = false)
    @JoinColumn(name = "currency_code", nullable = false)
    private Currency currency;
    @Column(name = "shares")
    private double shares;
    @Column(name = "shares_value_in_dollars")
    private double sharesValueInDollars;
    @Column(name = "asset_wallet_dollars")
    private double assetWalletDollars;
    @Column(name = "total_value_in_dollars")
    private double totalValueInDollars;
    @Column(name = "target_price")
    private double targetPrice;
    // TODO: Add buying strategy which may sell the whole asset, only the
    //       profits, or a set amount or percentage of the asset.
    //===========================-Constructors-===============================
    public PortfolioAsset() {
        this.currency = SupportedCurrencies.BITCOIN;
        this.shares = 0;
        this.sharesValueInDollars = 0;
        this.assetWalletDollars = 0;
        this.fetchTotalValueInDollars();
        this.targetPrice = 0;
    }
    public PortfolioAsset(Portfolio portfolio, Currency currency, double shares, double assetWalletDollars) {
        this.portfolio = portfolio;
        this.currency = currency;
        this.shares = shares;
        this.assetWalletDollars = assetWalletDollars;
        this.portfolio.addAsset(this);
        this.fetchTotalValueInDollars();
    }
    public PortfolioAsset(Currency currency, double shares, double assetWalletDollars) {
        this.currency = currency;
        this.shares = shares;
        this.assetWalletDollars = assetWalletDollars;
        this.fetchTotalValueInDollars();
    }
    //=============================-Methods-==================================

    //---------------------------Update-Values--------------------------------
    public void updateValues() {
        this.fetchSharesValueInDollars();
        this.fetchTotalValueInDollars();
    }
    //--------------------Fetch-Total-Value-In-Dollars------------------------
    public void fetchTotalValueInDollars() {
        double sharesValue = this.shares * this.currency.getValue();
        this.totalValueInDollars = sharesValue + this.assetWalletDollars;
    }
    public void fetchSharesValueInDollars() {
        this.sharesValueInDollars = this.shares * this.currency.getValue();
    }
    public boolean canSell() {
        return this.shares > 0;
    }
    public boolean canBuy() {
        return this.assetWalletDollars > 0;
    }
    //============================-Overrides-=================================

    //------------------------------Equals------------------------------------

    //------------------------------Hash-Code---------------------------------

    //------------------------------To-String---------------------------------

    //=============================-Getters-==================================

    //=============================-Setters-==================================

}
