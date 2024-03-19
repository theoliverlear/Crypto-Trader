package org.theoliverlear;
//=================================-Imports-==================================
import org.theoliverlear.entity.Portfolio;
import org.theoliverlear.entity.PortfolioAsset;
import org.theoliverlear.model.thread.TraderThread;
import org.theoliverlear.model.thread.ThreadManager;
import org.theoliverlear.model.trade.Trader;
import org.theoliverlear.update.SupportedCurrencies;

import java.util.ArrayList;

// TODO: Make this a component or service within the Spring framework. This
//       will allow for the JPA repository to be autowired and used to
//       persist the currency data.
public class CryptoTrader {
    //============================-Variables-=================================
    ArrayList<Trader> traders;
    ArrayList<Thread> traderThreads;
    ThreadManager traderThreadManager;
    //===========================-Constructors-===============================
    public CryptoTrader() {
        this.traders = new ArrayList<>();
        this.traderThreads = new ArrayList<>();
    }
    public CryptoTrader(ArrayList<Trader> traders) {
        this.traders = traders;
        this.traderThreads = new ArrayList<>();
        this.buildTraderThreads();
    }
    public void buildTraderThreads() {
        for (Trader trader : this.traders) {
            this.traderThreads.add(new TraderThread(trader));
        }
        this.traderThreadManager = new ThreadManager(this.traderThreads);
    }
    public void addTrader(Trader trader) {
        this.traders.add(trader);
    }
    public void addTraders(ArrayList<Trader> traders) {
        this.traders.addAll(traders);
    }
    public void startTraders() {
        this.traderThreadManager.startThreads();
    }
    public ArrayList<Trader> getTraders() {
        return this.traders;
    }
    //===============================-Main-===================================
    public static void main(String[] args) {
        Portfolio portfolio = new Portfolio();
        portfolio.getAssets().add(new PortfolioAsset(SupportedCurrencies.BITCOIN, 1.0, 0));
        Trader trader = new Trader(portfolio);
        ArrayList<Trader> traders = new ArrayList<>();
        traders.add(trader);
        CryptoTrader cryptoTrader = new CryptoTrader(traders);
        cryptoTrader.startTraders();
    }
}
