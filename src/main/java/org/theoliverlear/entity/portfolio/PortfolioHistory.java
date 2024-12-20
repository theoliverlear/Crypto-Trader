package org.theoliverlear.entity.portfolio;
//=================================-Imports-==================================

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.theoliverlear.entity.Identifiable;
import org.theoliverlear.entity.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "portfolio_history")
@Builder
@Entity
public class PortfolioHistory extends Identifiable implements SequentiallyValuable<PortfolioHistory> {
    //============================-Variables-=================================
    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;
    @Column(name = "dollar_balance", columnDefinition = "DECIMAL(34, 18)")
    private double dollarBalance;
    @Column(name = "share_balance", columnDefinition = "DECIMAL(34, 18)")
    private double shareBalance;
    @Column(name = "total_worth", columnDefinition = "DECIMAL(34, 18)")
    private double totalWorth;
    @Column(name = "value_change", columnDefinition = "DECIMAL(34, 18)")
    private double valueChange;
    @Column(name = "trade_occurred")
    private boolean tradeOccurred;
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    //===========================-Constructors-===============================
    public PortfolioHistory() {
        super();
        this.dollarBalance = 0;
        this.shareBalance = 0;
        this.totalWorth = 0;
        this.lastUpdated = LocalDateTime.now();
    }
    public PortfolioHistory(Portfolio portfolio, boolean tradeOccurred) {
        super();
        this.portfolio = portfolio;
        this.dollarBalance = portfolio.getDollarBalance();
        this.shareBalance = portfolio.getShareBalance();
        this.totalWorth = portfolio.getTotalWorth();
        this.tradeOccurred = tradeOccurred;
        this.lastUpdated = LocalDateTime.now();
    }
    public PortfolioHistory(Portfolio portfolio, LocalDateTime lastUpdated) {
        super();
        this.portfolio = portfolio;
        this.dollarBalance = portfolio.getDollarBalance();
        this.shareBalance = portfolio.getShareBalance();
        this.totalWorth = portfolio.getTotalWorth();
        this.lastUpdated = lastUpdated;
    }
    //============================-Overrides-=================================

    public void calculateValueChange(PortfolioHistory previous) {
        if (previous == null) {
            this.valueChange = 0;
        } else {
            this.valueChange = this.totalWorth - previous.getTotalWorth();
        }
    }
    //------------------------------To-String---------------------------------
    @Override
    public String toString() {
        return "PortfolioHistory{" +
                "id=" + this.id +
                ", portfolio=" + this.portfolio +
                ", dollarBalance=" + this.dollarBalance +
                ", shareBalance=" + this.shareBalance +
                ", totalWorth=" + this.totalWorth +
                ", lastUpdated=" + this.lastUpdated +
                '}';
    }
}
