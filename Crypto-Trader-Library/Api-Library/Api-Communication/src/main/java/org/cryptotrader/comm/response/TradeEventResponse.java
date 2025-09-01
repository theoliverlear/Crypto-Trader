package org.cryptotrader.comm.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TradeEventResponse {
    private String currency;
    private double atPrice;
    private double valueChange;
    private double shareChange;
    private LocalDateTime tradeTime;
    private String tradeType;
    
    public TradeEventResponse(String currency,
                              double atPrice,
                              double valueChange,
                              double shareChange,
                              LocalDateTime tradeTime,
                              String tradeType) {
        this.currency = currency;
        this.atPrice = atPrice;
        this.valueChange = valueChange;
        this.shareChange = shareChange;
        this.tradeTime = tradeTime;
        this.tradeType = tradeType;
    }
}