package org.cryptotrader.api.repository;
//=================================-Imports-==================================
import org.springframework.data.jpa.repository.JpaRepository;
import org.cryptotrader.entity.currency.CurrencyHistory;

public interface CurrencyHistoryRepository extends JpaRepository<CurrencyHistory, Long> {
    boolean existsByCurrencyCurrencyCode(String currencyCode);
}
