package org.cryptotrader.data.library.repository;

import org.cryptotrader.data.library.entity.currency.UniqueCurrencyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniqueCurrencyHistoryRepository extends JpaRepository<UniqueCurrencyHistory, Long> {
    boolean existsByCurrencyCurrencyCode(String currencyCode);
}
