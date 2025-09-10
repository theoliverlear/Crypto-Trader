package org.cryptotrader.api.library.services.models;

import org.cryptotrader.api.library.entity.currency.Currency;

import java.util.Map;

public interface MarketSnapshotOperations {
    void saveSnapshot(Map<String, Currency> currencies);
}
