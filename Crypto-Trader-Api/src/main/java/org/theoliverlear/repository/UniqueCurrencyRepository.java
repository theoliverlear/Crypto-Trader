package org.theoliverlear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.currency.UniqueCurrency;

public interface UniqueCurrencyRepository extends JpaRepository<UniqueCurrency, Long> {
    boolean existsByCurrency(String currencyCode);
}
