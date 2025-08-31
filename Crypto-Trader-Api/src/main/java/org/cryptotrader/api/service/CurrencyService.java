package org.cryptotrader.api.service;
//=================================-Imports-==================================
import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.comm.response.CurrencyValueResponse;
import org.cryptotrader.comm.response.CurrencyValuesListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.cryptotrader.component.CurrencyJsonGenerator;
import org.cryptotrader.component.MarketSnapshotsBackfiller;
import org.cryptotrader.entity.currency.*;
import org.cryptotrader.component.CurrencyDataRetriever;
import org.cryptotrader.repository.CurrencyHistoryRepository;
import org.cryptotrader.repository.CurrencyRepository;
import org.cryptotrader.repository.UniqueCurrencyHistoryRepository;
import org.cryptotrader.repository.UniqueCurrencyRepository;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CurrencyService {
    //============================-Variables-=================================
    private final CurrencyRepository currencyRepository;
    private final CurrencyHistoryRepository currencyHistoryRepository;
    private final UniqueCurrencyRepository uniqueCurrencyRepository;
    private final UniqueCurrencyHistoryRepository uniqueCurrencyHistoryRepository;
    private final CurrencyDataRetriever currencyDataRetriever;
    private final MarketSnapshotsBackfiller backfiller;
    private final MarketSnapshotService snapshotService;
    private final CurrencyJsonGenerator currencyJsonGenerator;

    //===========================-Constructors-===============================
    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository,
                           CurrencyHistoryRepository currencyHistoryRepository,
                           UniqueCurrencyRepository uniqueCurrencyRepository,
                           UniqueCurrencyHistoryRepository uniqueCurrencyHistoryRepository,
                           CurrencyDataRetriever currencyDataRetriever,
                           MarketSnapshotsBackfiller backfiller,
                           MarketSnapshotService snapshotService,
                           CurrencyJsonGenerator currencyJsonGenerator) {
        this.currencyRepository = currencyRepository;
        this.currencyHistoryRepository = currencyHistoryRepository;
        this.uniqueCurrencyRepository = uniqueCurrencyRepository;
        this.uniqueCurrencyHistoryRepository = uniqueCurrencyHistoryRepository;
        this.currencyDataRetriever = currencyDataRetriever;
        this.backfiller = backfiller;
        this.snapshotService = snapshotService;
        this.currencyJsonGenerator = currencyJsonGenerator;
    }
    //============================-Methods-===================================

    //--------------------------Save-Currencies-------------------------------
    @Async("taskExecutor")
    @Scheduled(fixedRate = 5000)
    public void saveCurrencies() {
        try {
            log.info("Updating currencies...");
            Map<String, Currency> currencies = this.currencyDataRetriever.getUpdatedCurrencies();
            for (Currency currency : SupportedCurrencies.SUPPORTED_CURRENCIES) {
                Currency previousCurrency = Currency.from(currency);
                String currencyCode = currency.getCurrencyCode();
                Currency updatedCurrency = currencies.get(currencyCode);
                currency.setValue(updatedCurrency.getValue());
                this.saveCurrency(currency);
                this.saveUniqueCurrencyIfNew(currency, previousCurrency, updatedCurrency);
            }
            this.snapshotService.saveSnapshot(currencies);
        } catch (NullPointerException exception) {
            log.error("Failed to update currencies. Regenerating JSON. Error: ", exception);
            this.currencyJsonGenerator.generateAndSave();
            SupportedCurrencies.loadCurrenciesFromJson();
        }
    }

    public CurrencyValueResponse toCurrencyValueResponse(Currency currency) {
        return new CurrencyValueResponse(currency.getName(),
                                         currency.getCurrencyCode(),
                                         currency.getValue());
    }

    public CurrencyValuesListResponse getCurrencyValuesResponse() {
        List<Currency> currencies = this.getAllCurrencies();
        return new CurrencyValuesListResponse(currencies.stream()
                                                        .map(this::toCurrencyValueResponse)
                                                        .toList());
    }

    public List<Currency> getAllCurrencies() {
        return this.currencyRepository.findAll();
    }

    public List<String> getAllCurrencyCodes() {
        return this.currencyRepository.findAllCurrencyCodes();
    }


    public void buildMarketSnapshots(boolean fullRefresh) {
        this.backfiller.buildSnapshots(fullRefresh);
    }

    public void saveCurrencyIfNew(Currency currency, Currency previousCurrency, Currency updatedCurrency) {
        if (this.hasCurrencyChanged(previousCurrency, updatedCurrency)) {
            this.saveCurrency(currency);
        }
    }

    private boolean hasCurrencyChanged(Currency previousCurrency, Currency updatedCurrency) {
        return previousCurrency.getValue() != updatedCurrency.getValue();
    }

    public void saveCurrency(Currency currency) {
        this.currencyRepository.save(currency);
        this.currencyHistoryRepository.save(new CurrencyHistory(currency, currency.getValue()));
    }

    public void saveUniqueCurrencyIfNew(Currency currency, Currency previousCurrency, Currency updatedCurrency) {
        if (!this.existsInUniqueCurrencyTable(currency.getCurrencyCode())) {
            this.saveUniqueCurrency(currency);
            return;
        }
        if (this.hasCurrencyChanged(previousCurrency, updatedCurrency)) {
            this.saveUniqueCurrency(currency);
        }
    }

    public void saveUniqueCurrency(Currency currency) {
        UniqueCurrency uniqueCurrency = new UniqueCurrency(currency);
        UniqueCurrencyHistory uniqueCurrencyHistory = new UniqueCurrencyHistory(currency);
        this.uniqueCurrencyRepository.save(uniqueCurrency);
        this.uniqueCurrencyHistoryRepository.save(uniqueCurrencyHistory);

    }

    //------------------------Get-Currency-By-Name----------------------------
    public Currency getCurrencyByName(String currencyName) {
        return this.currencyRepository.getCurrencyByName(currencyName);
    }
    //-------------------Get-Currency-By-Currency-Code------------------------
    public Currency getCurrencyByCurrencyCode(String currencyCode) {
        return this.currencyRepository.getCurrencyByCurrencyCode(currencyCode);
    }
    public boolean existsInCurrencyTable(String currencyCode) {
        return this.currencyRepository.existsByCurrencyCode(currencyCode);
    }
    public boolean existsInCurrencyHistoryTable(String currencyCode) {
        return this.currencyHistoryRepository.existsByCurrencyCurrencyCode(currencyCode);
    }
    public boolean existsInUniqueCurrencyTable(String currencyCode) {
        return this.uniqueCurrencyRepository.existsByCurrency(currencyCode);
    }
    public boolean existsInUniqueCurrencyHistoryTable(String currencyCode) {
        return this.uniqueCurrencyHistoryRepository.existsByCurrencyCurrencyCode(currencyCode);
    }
}
