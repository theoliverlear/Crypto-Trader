package org.cryptotrader.data.service;

import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.api.library.entity.currency.Currency;
import org.cryptotrader.api.library.entity.currency.SupportedCurrencies;
import org.cryptotrader.api.library.services.CurrencyService;
import org.cryptotrader.api.library.services.models.MarketSnapshotOperations;
import org.cryptotrader.api.library.component.CurrencyDataRetriever;
import org.cryptotrader.api.library.component.CurrencyJsonGenerator;
import org.cryptotrader.api.library.component.MarketSnapshotsBackfiller;
import org.cryptotrader.api.library.repository.CurrencyHistoryRepository;
import org.cryptotrader.api.library.repository.CurrencyRepository;
import org.cryptotrader.api.library.repository.UniqueCurrencyHistoryRepository;
import org.cryptotrader.api.library.repository.UniqueCurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CurrencyHarvesterService {
    //============================-Variables-=================================
    private final CurrencyRepository currencyRepository;
    private final CurrencyHistoryRepository currencyHistoryRepository;
    private final UniqueCurrencyRepository uniqueCurrencyRepository;
    private final UniqueCurrencyHistoryRepository uniqueCurrencyHistoryRepository;
    private final CurrencyDataRetriever currencyDataRetriever;
    private final MarketSnapshotsBackfiller backfiller;
    private final MarketSnapshotOperations snapshotService;
    private final CurrencyJsonGenerator currencyJsonGenerator;
    private final CurrencyService currencyService;

    //===========================-Constructors-===============================
    @Autowired
    public CurrencyHarvesterService(CurrencyRepository currencyRepository,
                                    CurrencyHistoryRepository currencyHistoryRepository,
                                    UniqueCurrencyRepository uniqueCurrencyRepository,
                                    UniqueCurrencyHistoryRepository uniqueCurrencyHistoryRepository,
                                    CurrencyDataRetriever currencyDataRetriever,
                                    MarketSnapshotsBackfiller backfiller,
                                    MarketSnapshotOperations snapshotService,
                                    CurrencyJsonGenerator currencyJsonGenerator,
                                    CurrencyService currencyService) {
        this.currencyRepository = currencyRepository;
        this.currencyHistoryRepository = currencyHistoryRepository;
        this.uniqueCurrencyRepository = uniqueCurrencyRepository;
        this.uniqueCurrencyHistoryRepository = uniqueCurrencyHistoryRepository;
        this.currencyDataRetriever = currencyDataRetriever;
        this.backfiller = backfiller;
        this.snapshotService = snapshotService;
        this.currencyJsonGenerator = currencyJsonGenerator;
        this.currencyService = currencyService;
    }
    //============================-Methods-===================================

    //--------------------------Save-Currencies-------------------------------
    @Scheduled(fixedRate = 5000)
    public void saveCurrencies() {
        try {
            log.info("Updating currencies...");
            Map<String, Currency> currencies = this.currencyDataRetriever.getUpdatedCurrencies();
            for (Currency currency : SupportedCurrencies.SUPPORTED_CURRENCIES) {
                try {
                    Currency previousCurrency = Currency.from(currency);
                    String currencyCode = currency.getCurrencyCode();
                    Currency updatedCurrency = currencies.get(currencyCode);
                    currency.setValue(updatedCurrency.getValue());
                    this.currencyService.saveCurrency(currency);
                    this.currencyService.saveUniqueCurrencyIfNew(currency, previousCurrency, updatedCurrency);
                } catch (NullPointerException exception) {
                    log.error("Problematic currency: {}", currency.getCurrencyCode());
//                    SupportedCurrencies.popCurrency(currency.getCurrencyCode());
                }
                
            }
            this.snapshotService.saveSnapshot(currencies);
        } catch (NullPointerException exception) {
            log.error("Failed to update currencies. Regenerating JSON. Error: ", exception);
        } catch (RuntimeException dbEx) {
            log.warn("Database unavailable during currency update; skipping this cycle: {}", dbEx.getMessage());
        }
    }

    public void buildMarketSnapshots(boolean fullRefresh) {
        this.backfiller.buildSnapshots(fullRefresh);
    }
}
