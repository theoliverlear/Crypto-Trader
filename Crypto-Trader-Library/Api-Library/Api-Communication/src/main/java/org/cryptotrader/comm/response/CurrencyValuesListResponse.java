package org.cryptotrader.comm.response;

import lombok.Data;

import java.util.List;

@Data
public class CurrencyValuesListResponse {
    private List<CurrencyValueResponse> currencies;
    public CurrencyValuesListResponse(List<CurrencyValueResponse> currencies) {
        this.currencies = currencies;
    }
}
