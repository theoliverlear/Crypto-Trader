package org.cryptotrader.data.library.entity.currency.builder.models;

import org.cryptotrader.data.library.entity.currency.Currency;
import org.cryptotrader.universal.library.model.BuilderFactory;

import java.time.LocalDateTime;

/**
 * Abstract base class for building {@link Currency} instances.
 *
 * @param <T> concrete builder type
 * @see Currency
 * @see BuilderFactory
 * @author Oliver Lear Sigwarth (theoliverlear)
 */
public abstract class AbstractCurrency implements BuilderFactory<Currency> {
    public abstract AbstractCurrency name(String name);
    public abstract AbstractCurrency currencyCode(String currencyCode);
    public abstract AbstractCurrency urlPath(String urlPath);
    public abstract AbstractCurrency value(double value);
    public abstract AbstractCurrency lastUpdated(LocalDateTime lastUpdated);
}
