package org.cryptotrader.api.library.entity.currency.builder;

import org.cryptotrader.api.library.entity.currency.Currency;
import org.cryptotrader.api.library.entity.currency.builder.models.AbstractCurrency;

import java.time.LocalDateTime;

public class CurrencyBuilder extends AbstractCurrency {
    private String name;
    private String currencyCode;
    private String urlPath;
    private double value;
    private LocalDateTime lastUpdated;

    public CurrencyBuilder() {
        this.name = "";
        this.currencyCode = "";
        this.urlPath = "";
        this.value = 0;
        this.lastUpdated = LocalDateTime.now();
    }
    @Override
    public CurrencyBuilder name(String name) {
        this.name = name;
        return this;
    }
    @Override
    public CurrencyBuilder currencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }
    @Override
    public CurrencyBuilder urlPath(String urlPath) {
        this.urlPath = urlPath;
        return this;
    }
    @Override
    public CurrencyBuilder value(double value) {
        this.value = value;
        return this;
    }
    @Override
    public CurrencyBuilder lastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }
    @Override
    public Currency build() {
        return new Currency(this.name,
                            this.currencyCode,
                            this.urlPath,
                            this.value,
                            this.lastUpdated);
    }
}
