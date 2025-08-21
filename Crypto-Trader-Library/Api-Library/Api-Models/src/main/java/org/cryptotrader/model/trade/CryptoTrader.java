package org.cryptotrader.model.trade;
//=================================-Imports-==================================
import lombok.Getter;
import lombok.Setter;
import org.cryptotrader.entity.portfolio.Portfolio;

import java.util.ArrayList;
import java.util.List;

// TODO: Make this a component or service within the Spring framework. This
//       will allow for the JPA repository to be autowired and used to
//       persist the currency data.
@Getter
@Setter
public class CryptoTrader {
    //============================-Variables-=================================
    private List<Trader> traders;
    //===========================-Constructors-===============================
    public CryptoTrader() {
        this.traders = new ArrayList<>();
    }
    public CryptoTrader(List<Trader> traders) {
        this.traders = traders;
    }
    //=============================-Methods-==================================

    //------------------------Trade-All-Portfolios----------------------------
    public void tradeAllPortfolios() {
        for (Trader trader : this.traders) {
            trader.tradeAllAssets();
        }
    }
    //---------------------------Add-Portfolio--------------------------------
    public void addTrader(Trader trader) {
        this.traders.add(trader);
    }
    //----------------------------Add-Traders---------------------------------
    public void addTraders(ArrayList<Trader> traders) {
        this.traders.addAll(traders);
    }
    //-------------------------Add-All-Portfolios-----------------------------
    public void addAllPortfolios(List<Portfolio> portfolios) {
        for (Portfolio portfolio : portfolios) {
            this.addTrader(new Trader(portfolio));
        }
    }
    //--------------------------------Size------------------------------------
    public int size() {
        return this.traders.size();
    }
    //------------------------------Is-Empty----------------------------------
    public boolean isEmpty() {
        return this.traders.isEmpty();
    }
}
