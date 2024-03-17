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
    @Transient
    private ArrayList<PortfolioAsset> assets;
    public Portfolio() {
        this.assets = new ArrayList<>();
    }
    public ArrayList<PortfolioAsset> getAssets() {
        return this.assets;
    }
    public void setAssets(ArrayList<PortfolioAsset> assets) {
        this.assets = assets;
    }
}
