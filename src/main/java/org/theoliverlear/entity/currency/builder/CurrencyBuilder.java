package org.theoliverlear.entity.currency.builder;

import org.theoliverlear.entity.currency.Currency;
import org.theoliverlear.entity.currency.builder.models.AbstractCurrency;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class CurrencyBuilder extends AbstractCurrency {
    private String name;
    private String currencyCode;
    private String urlPath;
    private double value;
    private String formattedValue;
    private LocalDateTime lastUpdated;
    private static final DecimalFormat decimalFormat = new DecimalFormat("##,#00.00000000");

    public CurrencyBuilder() {
        this.name = "";
        this.currencyCode = "";
        this.urlPath = "";
        this.value = 0;
        this.formattedValue = decimalFormat.format(this.value);
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
        this.formattedValue = decimalFormat.format(value);
        return this;
    }
    @Override
    public CurrencyBuilder formattedValue(String formattedValue) {
        this.formattedValue = formattedValue;
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
                            this.formattedValue,
                            this.lastUpdated);
    }
}
