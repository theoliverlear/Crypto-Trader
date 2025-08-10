package org.theoliverlear.service;
//=================================-Imports-==================================
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.theoliverlear.component.CurrencyJsonGenerator;
import org.theoliverlear.component.MarketSnapshotsBackfiller;
import org.theoliverlear.entity.currency.*;
import org.theoliverlear.component.CurrencyDataRetriever;
import org.theoliverlear.repository.CurrencyHistoryRepository;
import org.theoliverlear.repository.CurrencyRepository;
import org.theoliverlear.repository.UniqueCurrencyHistoryRepository;
import org.theoliverlear.repository.UniqueCurrencyRepository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
