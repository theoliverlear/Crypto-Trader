package org.theoliverlear.service;
//=================================-Imports-==================================
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theoliverlear.entity.currency.Currency;
import org.theoliverlear.entity.currency.CurrencyHistory;
import org.theoliverlear.repository.CurrencyHistoryRepository;
import org.theoliverlear.repository.CurrencyRepository;
import org.theoliverlear.entity.currency.SupportedCurrencies;

@Service
public class CurrencyService {
    //============================-Variables-=================================
    private CurrencyRepository currencyRepository;
    private CurrencyHistoryRepository currencyHistoryRepository;
    //===========================-Constructors-===============================
    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository,
                           CurrencyHistoryRepository currencyHistoryRepository) {
        this.currencyRepository = currencyRepository;
        this.currencyHistoryRepository = currencyHistoryRepository;
    }
    //============================-Methods-===================================

    //--------------------------Save-Currencies-------------------------------
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
    //------------------------Get-Currency-By-Name----------------------------
    public Currency getCurrencyByName(String currencyName) {
        return this.currencyRepository.getCurrencyByName(currencyName);
    }
    //-------------------Get-Currency-By-Currency-Code------------------------
    public Currency getCurrencyByCurrencyCode(String currencyCode) {
        return this.currencyRepository.getCurrencyByCurrencyCode(currencyCode);
    }
}
