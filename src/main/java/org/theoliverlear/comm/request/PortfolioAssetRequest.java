package org.theoliverlear.comm.request;
//=================================-Imports-==================================
import lombok.Data;

@Data
public class PortfolioAssetRequest {
    //============================-Variables-=================================
    String currencyName;
    double shares;
    double walletDollars;
    //===========================-Constructors-===============================
    public PortfolioAssetRequest(String currencyName, double shares, double walletDollars) {
        this.currencyName = currencyName;
        this.shares = shares;
        this.walletDollars = walletDollars;
    }
}
