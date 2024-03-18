package org.theoliverlear.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;
import java.util.ArrayList;
@Entity
@Table(name = "portfolios")
public class Portfolio {
    @Id
    private Long userId;
    double dollarBalance;
    double shareBalance;
    double totalWorth;
    LocalDateTime lastUpdated;
    @Transient
    private ArrayList<PortfolioAsset> assets;
    //===========================-Constructors-===============================
    public Portfolio() {
        this.assets = new ArrayList<>();
    }
    public Portfolio(Long userId) {
        this.userId = userId;
        this.dollarBalance = 0;
        this.shareBalance = 0;
        this.totalWorth = 0;
    }
    public Portfolio(Long userId, double dollarBalance, double shareBalance, double totalWorth) {
        this.userId = userId;
        this.dollarBalance = dollarBalance;
        this.shareBalance = shareBalance;
        this.totalWorth = totalWorth;
    }
    public Portfolio(Long userId,ArrayList<PortfolioAsset> assets) {
        this(userId);
        this.assets = assets;
    }
    //=============================-Methods-==================================
    public void updateValues() { // TODO: Make an Updatable interface
        this.dollarBalance = 0;
        this.shareBalance = 0;
        for (PortfolioAsset asset : this.assets) {
            this.dollarBalance += asset.getAssetWalletDollars();
            this.shareBalance += asset.getSharesValueInDollars();
        }
        this.totalWorth = this.dollarBalance + this.shareBalance;
        this.lastUpdated = LocalDateTime.now();
    }
    //============================-Overrides-=================================

    //------------------------------Equals------------------------------------
    @Override
    public boolean equals(Object object) {
        if (object instanceof Portfolio portfolio) {
            boolean sameUserId = this.userId.equals(portfolio.userId);
            boolean sameDollarBalance = this.dollarBalance == portfolio.dollarBalance;
            boolean sameShareBalance = this.shareBalance == portfolio.shareBalance;
            boolean sameTotalWorth = this.totalWorth == portfolio.totalWorth;
            boolean sameLastUpdated = this.lastUpdated.equals(portfolio.lastUpdated);
            boolean sameAssets = this.assets.equals(portfolio.assets);
            return  sameUserId && sameDollarBalance && sameShareBalance &&
                    sameTotalWorth && sameLastUpdated && sameAssets;
        }
        return false;
    }
    //------------------------------Hash-Code---------------------------------

    //------------------------------To-String---------------------------------

    //=============================-Getters-==================================
    public ArrayList<PortfolioAsset> getAssets() {
        return this.assets;
    }
    //=============================-Setters-==================================
    public void setAssets(ArrayList<PortfolioAsset> assets) {
        this.assets = assets;
    }
}
