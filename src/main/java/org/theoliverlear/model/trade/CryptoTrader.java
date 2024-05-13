package org.theoliverlear.model.trade;
//=================================-Imports-==================================
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

// TODO: Make this a component or service within the Spring framework. This
//       will allow for the JPA repository to be autowired and used to
//       persist the currency data.
@Getter
@Setter
public class CryptoTrader {
    //============================-Variables-=================================
    ArrayList<Trader> traders;
    //===========================-Constructors-===============================
    public CryptoTrader() {
        this.traders = new ArrayList<>();
    }
    public CryptoTrader(ArrayList<Trader> traders) {
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
    //--------------------------------Size------------------------------------
    public int size() {
        return this.traders.size();
    }
    //------------------------------Is-Empty----------------------------------
    public boolean isEmpty() {
        return this.traders.isEmpty();
    }
}
