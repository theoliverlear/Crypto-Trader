package org.cryptotrader.api.library.repository;

import org.cryptotrader.api.library.entity.currency.UniqueCurrencyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniqueCurrencyHistoryRepository extends JpaRepository<UniqueCurrencyHistory, Long> {
    boolean existsByCurrencyCurrencyCode(String currencyCode);
}
