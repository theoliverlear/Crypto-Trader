package org.theoliverlear.repository;
//=================================-Imports-==================================
import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.currency.CurrencyHistory;

public interface CurrencyHistoryRepository extends JpaRepository<CurrencyHistory, Long> {
    boolean existsByCurrencyCurrencyCode(String currencyCode);
}
