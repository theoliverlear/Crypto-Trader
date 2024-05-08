package org.theoliverlear.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.theoliverlear.convert.PortfolioAssetArrayListConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @JoinColumn(name = "portfolio_assets")
    private ArrayList<PortfolioAsset> assets;
    //===========================-Constructors-===============================
    public Portfolio() {
        this.user = new User();
        this.assets = new ArrayList<>();
    }
    public Portfolio(User user) {
        this.user = user;
        this.assets = new ArrayList<>();
    }
    public Portfolio(User user, ArrayList<PortfolioAsset> assets) {
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
    }
    public Portfolio(Long id, double dollarBalance, double shareBalance, double totalWorth) {
        this.user = new User();
        this.id = id;
        this.dollarBalance = dollarBalance;
        this.shareBalance = shareBalance;
        this.totalWorth = totalWorth;
    }
    public Portfolio(Long id, ArrayList<PortfolioAsset> assets) {
        this(id);
        this.user = new User();
        this.assets = assets;
        this.updateValues();
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
    public void addAsset(PortfolioAsset asset) {
        this.assets.add(asset);
        this.updateValues();
    }
    public boolean removeAsset(PortfolioAsset asset) {
        boolean removed = this.assets.remove(asset);
        if (removed) {
            this.updateValues();
        }
        return removed;
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
