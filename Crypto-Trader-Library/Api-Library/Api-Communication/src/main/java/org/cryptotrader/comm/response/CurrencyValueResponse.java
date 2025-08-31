package org.cryptotrader.comm.response;

import lombok.Data;

@Data
public class CurrencyValueResponse {
    private String currencyName;
    private String currencyCode;
    private double value;
    public CurrencyValueResponse(String currencyName,
                                 String currencyCode,
                                 double value) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.value = value;
    }
}
