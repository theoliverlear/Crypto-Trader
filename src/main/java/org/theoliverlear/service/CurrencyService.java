package org.theoliverlear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theoliverlear.entity.Currency;
import org.theoliverlear.repository.CurrencyRepository;
import org.theoliverlear.update.CurrencyUpdater;

@Service
public class CurrencyService {
    CurrencyRepository currencyRepository;
    CurrencyUpdater currencyUpdater;
    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
        this.currencyUpdater = new CurrencyUpdater();
        this.currencyUpdater.startCurrencyUpdaters();
    }
    public void saveCurrency(Currency currency) {
        this.currencyRepository.save(currency);
    }
}
