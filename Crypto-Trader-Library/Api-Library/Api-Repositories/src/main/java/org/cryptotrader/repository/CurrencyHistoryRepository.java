package org.cryptotrader.repository;
//=================================-Imports-==================================

import org.cryptotrader.entity.currency.CurrencyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyHistoryRepository extends JpaRepository<CurrencyHistory, Long> {
    boolean existsByCurrencyCurrencyCode(String currencyCode);
}
