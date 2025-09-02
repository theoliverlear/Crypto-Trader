package org.cryptotrader.entity.trade;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.cryptotrader.entity.Identifiable;
import org.cryptotrader.entity.portfolio.PortfolioAssetHistory;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "trade_events")
public class TradeEvent extends Identifiable {
    @JoinColumn(name = "portfolio_asset_id", nullable = false)
    @OneToOne
    private PortfolioAssetHistory assetHistory;
    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type", length = 50, nullable = false)
    private TradeType tradeType;
    @Column(name = "value_change", columnDefinition = "DECIMAL(34, 18)")
    private double valueChange;
    @Column(name = "shares_change", columnDefinition = "DECIMAL(34, 18)")
    private double sharesChange;
    @Column(name = "trade_time", nullable = false)
    private LocalDateTime tradeTime;
    
    public TradeEvent() {
        super();
    }
    
    public TradeEvent(PortfolioAssetHistory assetHistory,
                           TradeType tradeType,
                           double valueChange,
                           double sharesChange) {
        this(assetHistory, tradeType, valueChange, sharesChange, LocalDateTime.now());
    }
    public TradeEvent(PortfolioAssetHistory assetHistory,
                      TradeType tradeType,
                      double valueChange,
                      double sharesChange,
                      LocalDateTime tradeTime) {
        super();
        this.assetHistory = assetHistory;
        this.tradeType = tradeType;
        this.valueChange = valueChange;
        this.sharesChange = sharesChange;
        this.tradeTime = tradeTime;
    }
}
