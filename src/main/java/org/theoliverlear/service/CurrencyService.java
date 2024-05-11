package org.theoliverlear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theoliverlear.entity.Currency;
import org.theoliverlear.entity.CurrencyHistory;
import org.theoliverlear.repository.CurrencyHistoryRepository;
import org.theoliverlear.repository.CurrencyRepository;
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
    @Async("taskExecutor")
    @Scheduled(fixedRate = 5000)
    public void saveCurrencies() {
        for (Currency currency : SupportedCurrencies.SUPPORTED_CURRENCIES) {
            Currency previousCurrency = Currency.from(currency);
            currency.updateValue();
            if (!previousCurrency.equals(currency)) {
                this.currencyRepository.save(currency);
                this.currencyHistoryRepository.save(new CurrencyHistory(currency, currency.getValue()));
            }
        }
    }
}
