package org.theoliverlear.comm.request;

import lombok.Data;

@Data
public class PortfolioAssetRequest {
    String currencyName;
    double shares;
    double walletDollars;

    public PortfolioAssetRequest(String currencyName, double shares, double walletDollars) {
        this.currencyName = currencyName;
        this.shares = shares;
        this.walletDollars = walletDollars;
    }
}
