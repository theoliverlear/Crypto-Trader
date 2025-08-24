package org.cryptotrader.repository;

import org.cryptotrader.entity.currency.UniqueCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniqueCurrencyRepository extends JpaRepository<UniqueCurrency, Long> {
    boolean existsByCurrency(String currencyCode);
}
