package org.theoliverlear.update;

import org.springframework.stereotype.Service;
import org.theoliverlear.entity.Currency;
import org.theoliverlear.service.CurrencyService;

@Service
public class CurrencyUpdater {
//    ArrayList<Thread> currencyUpdaters;
//    ThreadManager currencyUpdaterThreadManager;
    CurrencyService currencyService;
    public CurrencyUpdater(CurrencyService currencyService) {
//        this.currencyUpdaters = new ArrayList<>();
        this.startCurrencyUpdaters();
    }
    public void startCurrencyUpdaters() {
        for (final Currency currency : SupportedCurrencies.SUPPORTED_CURRENCIES) {
//            this.currencyService.saveCurrencies(currency);
//            this.currencyUpdaters.add(new CurrencyUpdaterThread(currency, this.currencyService));
        }
//        this.currencyUpdaterThreadManager = new ThreadManager(this.currencyUpdaters);
//        this.currencyUpdaterThreadManager.startThreads();
    }
}
