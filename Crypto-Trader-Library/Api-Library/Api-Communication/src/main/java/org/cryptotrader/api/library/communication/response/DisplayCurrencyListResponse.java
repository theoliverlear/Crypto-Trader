package org.cryptotrader.api.library.communication.response;

import lombok.Data;

import java.util.List;

@Data
public class DisplayCurrencyListResponse {
    private List<DisplayCurrencyResponse> currencies;
    public DisplayCurrencyListResponse(List<DisplayCurrencyResponse> currencies) {
        this.currencies = currencies;
    }
}
