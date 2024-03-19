package org.theoliverlear.model.thread;

import org.theoliverlear.model.trade.Trader;

public class TraderThread extends Thread {
    Trader trader;
    public TraderThread(Trader trader) {
        this.trader = trader;
    }
    @Override
    public void run() {
        while (true) {
            try {
                this.trader.tradeAllAssets();
                this.trader.getPortfolio().updateValues();
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
