package org.theoliverlear.entity.currency.builder.models;

import org.theoliverlear.entity.currency.Currency;
import org.theoliverlear.model.BuilderFactory;

import java.time.LocalDateTime;

public abstract class AbstractCurrency implements BuilderFactory<Currency> {
    public abstract AbstractCurrency name(String name);
    public abstract AbstractCurrency currencyCode(String currencyCode);
    public abstract AbstractCurrency urlPath(String urlPath);
    public abstract AbstractCurrency value(double value);
    public abstract AbstractCurrency formattedValue(String formattedValue);
    public abstract AbstractCurrency lastUpdated(LocalDateTime lastUpdated);
}
