package org.theoliverlear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theoliverlear.entity.Currency;
import org.theoliverlear.entity.CurrencyHistory;
import org.theoliverlear.repository.CurrencyHistoryRepository;
import org.theoliverlear.repository.CurrencyRepository;
import org.theoliverlear.update.CurrencyUpdater;
import org.theoliverlear.update.SupportedCurrencies;

@Service
public class CurrencyService {
    CurrencyRepository currencyRepository;
    CurrencyHistoryRepository currencyHistoryRepository;
    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository,
                           CurrencyHistoryRepository currencyHistoryRepository) {
        this.currencyRepository = currencyRepository;
        this.currencyHistoryRepository = currencyHistoryRepository;
    }
    @Scheduled(fixedRate = 5000)
    public void saveCurrencies() {
        for (final Currency currency : SupportedCurrencies.SUPPORTED_CURRENCIES) {
            Currency previousCurrency = Currency.from(currency);
            currency.updateValue();
            System.out.println(currency);
            if (!previousCurrency.equals(currency)) {
                this.currencyRepository.saveCurrencyByCurrencyCode(currency);
                CurrencyHistory currencyHistory = new CurrencyHistory(currency, currency.getValue());
                this.currencyHistoryRepository.save(currencyHistory);
            }
        }
    }
}
