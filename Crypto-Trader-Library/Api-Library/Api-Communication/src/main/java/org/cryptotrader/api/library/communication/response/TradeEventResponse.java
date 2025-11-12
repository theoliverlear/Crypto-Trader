package org.cryptotrader.api.library.communication.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TradeEventResponse {
    private String currency;
    private double valueChange;
    private double sharesChange;
    private LocalDateTime tradeTime;
    private String tradeType;
    
    public TradeEventResponse(String currency,
                              double valueChange,
                              double sharesChange,
                              LocalDateTime tradeTime,
                              String tradeType) {
        this.currency = currency;
        this.valueChange = valueChange;
        this.sharesChange = sharesChange;
        this.tradeTime = tradeTime;
        this.tradeType = tradeType;
    }
}