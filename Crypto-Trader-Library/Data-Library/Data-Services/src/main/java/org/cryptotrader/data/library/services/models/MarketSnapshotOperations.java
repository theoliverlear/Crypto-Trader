package org.cryptotrader.data.library.services.models;

import org.cryptotrader.data.library.entity.currency.Currency;

import java.util.Map;

public interface MarketSnapshotOperations {
    void saveSnapshot(Map<String, Currency> currencies);
}
