package org.theoliverlear.entity.portfolio;
//=================================-Imports-==================================
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.theoliverlear.entity.Identifiable;
import org.theoliverlear.entity.portfolio.builder.PortfolioBuilder;
import org.theoliverlear.entity.user.ProductUser;
import org.theoliverlear.entity.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "portfolios")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Portfolio extends Identifiable implements UpdatableValues {
    //============================-Variables-=================================
    @JsonBackReference
    @OneToOne(mappedBy = "portfolio")
    private ProductUser user;
    @Column(name = "dollar_balance", columnDefinition = "DECIMAL(34, 18)")
    private double dollarBalance;
    @Column(name = "share_balance", columnDefinition = "DECIMAL(34, 18)")
    private double shareBalance;
    @Column(name = "total_worth", columnDefinition = "DECIMAL(34, 18)")
    private double totalWorth;
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    @OneToMany(mappedBy = "portfolio", fetch = FetchType.EAGER)
    private List<PortfolioAsset> assets;
    @OneToMany(mappedBy = "portfolio", fetch = FetchType.EAGER)
    private List<PortfolioHistory> portfolioHistory;
    //===========================-Constructors-===============================
    public Portfolio() {
        super();
        this.dollarBalance = 0;
        this.shareBalance = 0;
        this.totalWorth = 0;
        this.user = new ProductUser();
        this.assets = new ArrayList<>();
        this.portfolioHistory = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }
    public Portfolio(ProductUser user) {
        super();
        this.user = user;
        this.assets = new ArrayList<>();
        this.portfolioHistory = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }
    public Portfolio(ProductUser user, List<PortfolioAsset> assets) {
        super();
        this.user = user;
        this.assets = assets;
        this.portfolioHistory = new ArrayList<>();
        this.updateValues();
    }
    public Portfolio(double dollarBalance, double shareBalance, double totalWorth) {
        super();
        this.user = new ProductUser();
        this.dollarBalance = dollarBalance;
        this.shareBalance = shareBalance;
        this.totalWorth = totalWorth;
        this.assets = new ArrayList<>();
        this.portfolioHistory = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }

    public Portfolio(List<PortfolioAsset> assets) {
        super();
        this.user = new ProductUser();
        this.assets = assets;
        this.lastUpdated = LocalDateTime.now();
        this.portfolioHistory = new ArrayList<>();
        this.updateValues();
    }

    public Portfolio(ProductUser user,
                     double dollarBalance,
                     double shareBalance,
                     double totalWorth,
                     LocalDateTime lastUpdated,
                     List<PortfolioAsset> assets,
                     List<PortfolioHistory> portfolioHistory) {
        super();
        this.user = user;
        this.dollarBalance = dollarBalance;
        this.shareBalance = shareBalance;
        this.totalWorth = totalWorth;
        this.lastUpdated = lastUpdated;
        this.assets = assets;
        this.portfolioHistory = portfolioHistory;
        this.updateValues();
    }
    
    //=============================-Methods-==================================

    //-----------------------Add-Portfolio-History----------------------------
    public void addPortfolioHistory(PortfolioHistory portfolioHistory) {
        this.portfolioHistory.add(portfolioHistory);
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
    //--------------------------------From------------------------------------
    public static Portfolio from(Portfolio portfolio) {
        Portfolio newPortfolio = new Portfolio();
        newPortfolio.setId(portfolio.getId());
        newPortfolio.setDollarBalance(portfolio.getDollarBalance());
        newPortfolio.setShareBalance(portfolio.getShareBalance());
        newPortfolio.setTotalWorth(portfolio.getTotalWorth());
        newPortfolio.setLastUpdated(portfolio.getLastUpdated());
        newPortfolio.setAssets(portfolio.getAssets());
        return newPortfolio;
    }

    public static double getTotalPortfolioValue(List<PortfolioAsset> assets) {
        double dollarBalance = getTotalDollarValue(assets);
        double shareBalance = getTotalShareValue(assets);
//        for (PortfolioAsset asset : assets) {
//            asset.updateValues();
//            dollarBalance += asset.getAssetWalletDollars();
//            shareBalance += asset.getSharesValueInDollars();
//        }
        return dollarBalance + shareBalance;
    }

    public static double getTotalDollarValue(List<PortfolioAsset> assets) {
        double dollarBalance = 0;
        for (PortfolioAsset asset : assets) {
            asset.updateValues();
            dollarBalance += asset.getAssetWalletDollars();
        }
        return dollarBalance;
    }

    public static double getTotalShareValue(List<PortfolioAsset> assets) {
        double shareBalance = 0;
        for (PortfolioAsset asset : assets) {
            asset.updateValues();
            shareBalance += asset.getSharesValueInDollars();
        }
        return shareBalance;
    }

    public static PortfolioBuilder builder() {
        return new PortfolioBuilder();
    }

    //============================-Overrides-=================================

    //---------------------------Update-Values--------------------------------
    @Override
    public void updateValues() {
        this.dollarBalance = 0;
        this.shareBalance = 0;
        for (PortfolioAsset asset : this.assets) {
            asset.updateValues();
            this.dollarBalance += asset.getAssetWalletDollars();
            this.shareBalance += asset.getSharesValueInDollars();
        }
        this.totalWorth = this.dollarBalance + this.shareBalance;
        this.lastUpdated = LocalDateTime.now();
    }
    //------------------------------Equals------------------------------------
    @Override
    public boolean equals(Object object) {
        if (object instanceof Portfolio portfolio) {
            boolean sameDollarBalance = this.dollarBalance == portfolio.dollarBalance;
            boolean sameShareBalance = this.shareBalance == portfolio.shareBalance;
            boolean sameTotalWorth = this.totalWorth == portfolio.totalWorth;

            boolean sameAssets = this.assets.equals(portfolio.assets);
            if (this.id != null) {
                if (this.lastUpdated != null) {
                    boolean sameLastUpdated = this.lastUpdated.equals(portfolio.lastUpdated);
                    return sameDollarBalance && sameShareBalance &&
                            sameTotalWorth && sameLastUpdated && sameAssets;
                } else {
                    boolean sameId = this.id.equals(portfolio.id);
                    return sameId && sameDollarBalance && sameShareBalance &&
                            sameTotalWorth && sameAssets;
                }
            }
            return sameDollarBalance && sameShareBalance && sameTotalWorth &&
                    sameAssets;
        }
        return false;
    }
    //------------------------------Hash-Code---------------------------------

    //------------------------------To-String---------------------------------
    @Override
    public String toString() {
        StringBuilder portfolioString = new StringBuilder();
        portfolioString.append("Portfolio ID: ").append(this.id).append("\n");
        portfolioString.append("Dollar Balance: ").append(this.dollarBalance).append("\n");
        portfolioString.append("Share Balance: ").append(this.shareBalance).append("\n");
        portfolioString.append("Total Worth: ").append(this.totalWorth).append("\n");
        portfolioString.append("Assets: ").append("\n");
        List<PortfolioAsset> portfolioAssets = this.assets;
        for (int i = 0; i < portfolioAssets.size(); i++) {
            PortfolioAsset asset = portfolioAssets.get(i);
            if (i == portfolioAssets.size() - 1) {
                portfolioString.append(asset);
            } else {
                portfolioString.append(asset.toString()).append("\n");
            }
        }
        return portfolioString.toString();
    }
}
