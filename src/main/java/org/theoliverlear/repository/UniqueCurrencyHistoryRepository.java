package org.theoliverlear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.currency.UniqueCurrencyHistory;

public interface UniqueCurrencyHistoryRepository extends JpaRepository<UniqueCurrencyHistory, Long> {
    boolean existsByCurrencyCurrencyCode(String currencyCode);
}
