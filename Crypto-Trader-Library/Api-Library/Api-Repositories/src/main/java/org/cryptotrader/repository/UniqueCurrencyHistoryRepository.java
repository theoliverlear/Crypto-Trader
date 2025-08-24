package org.cryptotrader.repository;

import org.cryptotrader.entity.currency.UniqueCurrencyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniqueCurrencyHistoryRepository extends JpaRepository<UniqueCurrencyHistory, Long> {
    boolean existsByCurrencyCurrencyCode(String currencyCode);
}
