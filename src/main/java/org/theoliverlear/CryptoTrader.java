package org.theoliverlear;
//=================================-Imports-==================================
import org.theoliverlear.entity.Currency;
import org.theoliverlear.model.trade.Trader;
import org.theoliverlear.model.thread.CurrencyUpdaterThread;

import java.util.ArrayList;

// TODO: Make this a component or service within the Spring framework. This
//       will allow for the JPA repository to be autowired and used to
//       persist the currency data.
public class CryptoTrader {
    //============================-Variables-=================================
    ArrayList<Trader> traders;
    //===========================-Constructors-===============================
    public CryptoTrader() {
        this.traders = new ArrayList<>();
    }
    public void addTrader(Trader trader) {
        this.traders.add(trader);
    }
    public void startTraders() {
        for (Trader trader : this.traders) {
            trader.tradeAllAssets();
        }
    }
    //===============================-Main-===================================
    public static void main(String[] args) {
        Currency bitcoin = new Currency("Bitcoin", "BTC", "https://api.coinbase.com/v2/prices/BTC-USD/spot");
        CurrencyUpdaterThread currencyUpdaterThread = new CurrencyUpdaterThread(bitcoin);
        currencyUpdaterThread.start();
    }
}
