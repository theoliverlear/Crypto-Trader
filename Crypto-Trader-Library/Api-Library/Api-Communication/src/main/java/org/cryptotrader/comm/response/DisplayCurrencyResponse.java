package org.cryptotrader.comm.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisplayCurrencyResponse extends CurrencyValueResponse {
    private String logoUrl;
    
    public DisplayCurrencyResponse(String currencyName,
                                   String currencyCode,
                                   double value) {
        super(currencyName, currencyCode, value);
        this.logoUrl = this.generateUrl();
    }
    public DisplayCurrencyResponse(String currencyName,
                                   String currencyCode,
                                   double value,
                                   String logoUrl) {
        super(currencyName, currencyCode, value);
        this.logoUrl = logoUrl;
    }
    
    public String generateUrl() {
        final String baseUrl = "/assets/cryptofont/%s.svg";
        return String.format(baseUrl, this.getCurrencyCode().toLowerCase());
    }
}
