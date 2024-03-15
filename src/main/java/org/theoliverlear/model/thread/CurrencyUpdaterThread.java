package org.theoliverlear.model.thread;

import org.theoliverlear.entity.Currency;

public class CurrencyUpdaterThread extends Thread implements Runnable {
    Currency currency;
    public CurrencyUpdaterThread(Currency currency) {
        this.currency = currency;
    }
    @Override
    public void run() {
        while (true) {
            try {
                this.currency.updateValue();
                System.out.println(this.currency);
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
