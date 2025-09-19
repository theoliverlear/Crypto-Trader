package org.cryptotrader.api.library.services;
//=================================-Imports-==================================

import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.api.library.entity.currency.Currency;
import org.cryptotrader.api.library.entity.currency.CurrencyHistory;
import org.cryptotrader.api.library.entity.currency.UniqueCurrency;
import org.cryptotrader.api.library.entity.currency.UniqueCurrencyHistory;
import org.cryptotrader.api.library.model.currency.PerformanceRating;
import org.cryptotrader.api.library.services.models.MarketSnapshotOperations;
import org.cryptotrader.api.library.communication.response.DisplayCurrencyListResponse;
import org.cryptotrader.api.library.communication.response.DisplayCurrencyResponse;
import org.cryptotrader.api.library.communication.response.TimeValueResponse;
import org.cryptotrader.api.library.component.CurrencyDataRetriever;
import org.cryptotrader.api.library.component.CurrencyJsonGenerator;
import org.cryptotrader.api.library.component.MarketSnapshotsBackfiller;
import org.cryptotrader.api.library.repository.CurrencyHistoryRepository;
import org.cryptotrader.api.library.repository.CurrencyRepository;
import org.cryptotrader.api.library.repository.UniqueCurrencyHistoryRepository;
import org.cryptotrader.api.library.repository.UniqueCurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

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
    private final MarketSnapshotOperations snapshotService;
    private final CurrencyJsonGenerator currencyJsonGenerator;

    //===========================-Constructors-===============================
    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository,
                           CurrencyHistoryRepository currencyHistoryRepository,
                           UniqueCurrencyRepository uniqueCurrencyRepository,
                           UniqueCurrencyHistoryRepository uniqueCurrencyHistoryRepository,
                           CurrencyDataRetriever currencyDataRetriever,
                           MarketSnapshotsBackfiller backfiller,
                           MarketSnapshotOperations snapshotService,
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


    public DisplayCurrencyResponse toCurrencyValueResponse(Currency currency) {
        return new DisplayCurrencyResponse(currency.getName(),
                currency.getCurrencyCode(),
                currency.getValue());
    }

    public DisplayCurrencyListResponse getCurrencyValuesResponse() {
        List<Currency> currencies = this.getTopTenCurrencies();
        // sort by price desc
        currencies.sort((currencyOne, currencyTwo) -> {
            return Double.compare(currencyTwo.getValue(), currencyOne.getValue());
        });
        return new DisplayCurrencyListResponse(currencies.stream()
                .map(this::toCurrencyValueResponse)
                .toList());
    }

    public List<Currency> getTopTenCurrencies() {
        return this.currencyRepository.findTop10ByOrderByValueDesc();
    }
    
    public List<Currency> getAllCurrencies() {
        return this.currencyRepository.findAll();
    }

    public List<String> getAllCurrencyCodes() {
        return this.currencyRepository.findAllCurrencyCodes();
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

    public List<TimeValueResponse> getCurrencyHistory(String currencyCode, int hours) {
        return this.getCurrencyHistory(currencyCode, hours, 60);
    }
    
    public PerformanceRating getDayPerformance(String currencyCode) {
        double currentPrice = this.getCurrencyByCurrencyCode(currencyCode).getValue();
        double lastDayPrice = this.currencyHistoryRepository.getPreviousDayCurrency(currencyCode).getValue();
        return PerformanceRating.fromValues(lastDayPrice, currentPrice);
    }

    public String getPercentageDayPerformance(String currencyCode) {
        double currentPrice = this.getCurrencyByCurrencyCode(currencyCode).getValue();
        double lastDayPrice = this.currencyHistoryRepository.getPreviousDayCurrency(currencyCode).getValue();
        if (lastDayPrice == 0.0 || Double.isNaN(lastDayPrice) || Double.isNaN(currentPrice)) {
            return "0.00%";
        }
        double percentDelta = (currentPrice - lastDayPrice) / lastDayPrice * 100.0;
        return String.format("%+.2f%%", percentDelta);
    }
    
    public List<TimeValueResponse> getCurrencyHistory(String currencyCode, int hours, int intervalSeconds) {
        if (intervalSeconds <= 0) {
            intervalSeconds = 60;
        }
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        List<Object[]> rows = this.currencyHistoryRepository.findDownsampledHistory(currencyCode, since, intervalSeconds);
        return rows.stream()
                .map(record -> {
                    Object time = record[0];
                    String isoTime;
                    if (time instanceof Timestamp timestamp) {
                        isoTime = timestamp.toLocalDateTime().toString();
                    } else if (time instanceof LocalDateTime dateTime) {
                        isoTime = dateTime.toString();
                    } else {
                        isoTime = String.valueOf(time);
                    }
                    double valueAtTime = ((Number) record[1]).doubleValue();
                    return new TimeValueResponse(isoTime, valueAtTime);
                })
                .toList();
    }
}
