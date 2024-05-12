package org.theoliverlear.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "portfolios")
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToOne(mappedBy = "portfolio")
    User user;
    @Column(name = "dollar_balance")
    double dollarBalance;
    @Column(name = "share_balance")
    double shareBalance;
    @Column(name = "total_worth")
    double totalWorth;
    @Column(name = "last_updated")
    LocalDateTime lastUpdated;
    @OneToMany(mappedBy = "portfolio")
    private List<PortfolioAsset> assets;
    //===========================-Constructors-===============================
    public Portfolio() {
        this.user = new User();
        this.assets = new ArrayList<>();
    }
    public Portfolio(User user) {
        this.user = user;
        this.assets = new ArrayList<>();
    }
    public Portfolio(User user, List<PortfolioAsset> assets) {
        this.user = user;
        this.assets = assets;
        this.updateValues();
    }
    public Portfolio(Long id) {
        this.user = new User();
        this.id = id;
        this.dollarBalance = 0;
        this.shareBalance = 0;
        this.totalWorth = 0;
        this.assets = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }
    public Portfolio(Long id, double dollarBalance, double shareBalance, double totalWorth) {
        this.user = new User();
        this.id = id;
        this.dollarBalance = dollarBalance;
        this.shareBalance = shareBalance;
        this.totalWorth = totalWorth;
        this.assets = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }
    public Portfolio(Long id, List<PortfolioAsset> assets) {
        this(id);
        this.user = new User();
        this.assets = assets;
        this.lastUpdated = LocalDateTime.now();
        this.updateValues();
    }
    //=============================-Methods-==================================

    //---------------------------Update-Values--------------------------------
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
    //-----------------------------Add-Asset----------------------------------
    public void addAsset(PortfolioAsset asset) {
        this.assets.add(asset);
        this.updateValues();
    }
    //----------------------------Remove-Asset--------------------------------
    public boolean removeAsset(PortfolioAsset asset) {
        boolean removed = this.assets.remove(asset);
        if (removed) {
            this.updateValues();
        }
        return removed;
    }
    //-------------------------Is-Empty-Portfolio-----------------------------
    public boolean isEmpty() {
        return this.assets.isEmpty();
    }
    //============================-Overrides-=================================

    //------------------------------Equals------------------------------------
    @Override
    public boolean equals(Object object) {
        if (object instanceof Portfolio portfolio) {
            boolean sameDollarBalance = this.dollarBalance == portfolio.dollarBalance;
            boolean sameShareBalance = this.shareBalance == portfolio.shareBalance;
            boolean sameTotalWorth = this.totalWorth == portfolio.totalWorth;
            boolean sameLastUpdated = this.lastUpdated.equals(portfolio.lastUpdated);
            boolean sameAssets = this.assets.equals(portfolio.assets);
            if (this.id != null) {
                boolean sameId = this.id.equals(portfolio.id);
                return  sameId && sameDollarBalance && sameShareBalance &&
                        sameTotalWorth && sameLastUpdated && sameAssets;
            }
            return sameDollarBalance && sameShareBalance &&
                    sameTotalWorth && sameLastUpdated && sameAssets;
        }
        return false;
    }
    //------------------------------Hash-Code---------------------------------

    //------------------------------To-String---------------------------------

    //=============================-Getters-==================================

    //=============================-Setters-==================================

}
