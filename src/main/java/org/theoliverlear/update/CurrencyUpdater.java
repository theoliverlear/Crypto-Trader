package org.theoliverlear.update;

import org.theoliverlear.entity.Currency;
import org.theoliverlear.model.thread.CurrencyUpdaterThread;
import org.theoliverlear.model.thread.ThreadManager;

import java.util.ArrayList;

public class CurrencyUpdater {
    ArrayList<Thread> currencyUpdaters;
    ThreadManager currencyUpdaterThreadManager;
    public CurrencyUpdater() {
        this.currencyUpdaters = new ArrayList<>();
        this.startCurrencyUpdaters();
    }
    public void startCurrencyUpdaters() {
        for (final Currency currency : SupportedCurrencies.SUPPORTED_CURRENCIES) {
            this.currencyUpdaters.add(new CurrencyUpdaterThread(currency));
        }
        this.currencyUpdaterThreadManager = new ThreadManager(this.currencyUpdaters);
        this.currencyUpdaterThreadManager.startThreads();
    }
}
