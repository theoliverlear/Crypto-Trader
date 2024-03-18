package org.theoliverlear.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

//=================================-Imports-==================================
@Entity
public class PortfolioAsset {
    //============================-Variables-=================================
    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "currency_code", nullable = false)
    private Currency currency;
    private double shares;
    private double sharesValueInDollars;
    private double assetWalletDollars;
    private double totalValueInDollars;
    private double targetPrice;
    // TODO: Add buying strategy which may sell the whole asset, only the
    //       profits, or a set amount or percentage of the asset.
    //===========================-Constructors-===============================
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
    public Currency getCurrency() {
        return this.currency;
    }
    public double getShares() {
        return this.shares;
    }
    public double getSharesValueInDollars() {
        this.updateValues();
        return this.sharesValueInDollars;
    }
    public double getAssetWalletDollars() {
        this.updateValues();
        return this.assetWalletDollars;
    }
    public double getTotalValueInDollars() {
        this.updateValues();
        return this.totalValueInDollars;
    }
    public double getTargetPrice() {
        return this.targetPrice;
    }
    //=============================-Setters-==================================
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    public void setShares(double shares) {
        this.shares = shares;
    }
    public void setSharesValueInDollars(double sharesValueInDollars) {
        this.sharesValueInDollars = sharesValueInDollars;
    }
    public void setAssetWalletDollars(double assetWalletDollars) {
        this.assetWalletDollars = assetWalletDollars;
    }
    public void setTotalValueInDollars(double totalValueInDollars) {
        this.totalValueInDollars = totalValueInDollars;
    }
    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }
}
