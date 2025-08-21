package org.cryptotrader.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.cryptotrader.entity.currency.UniqueCurrencyHistory;

public interface UniqueCurrencyHistoryRepository extends JpaRepository<UniqueCurrencyHistory, Long> {
    boolean existsByCurrencyCurrencyCode(String currencyCode);
}
