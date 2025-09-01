package org.cryptotrader.entity.trade;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    private PortfolioAssetHistory assetHistory;
    private TradeType tradeType;
    private double valueChange;
    private double sharesChange;
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
