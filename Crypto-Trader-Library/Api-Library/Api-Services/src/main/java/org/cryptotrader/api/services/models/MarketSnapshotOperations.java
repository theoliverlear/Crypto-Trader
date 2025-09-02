package org.cryptotrader.api.services.models;

import org.cryptotrader.entity.currency.Currency;

import java.util.Map;

public interface MarketSnapshotOperations {
    void saveSnapshot(Map<String, Currency> currencies);
}
