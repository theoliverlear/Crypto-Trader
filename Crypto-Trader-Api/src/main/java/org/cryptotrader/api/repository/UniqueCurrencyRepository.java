package org.cryptotrader.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.cryptotrader.entity.currency.UniqueCurrency;

public interface UniqueCurrencyRepository extends JpaRepository<UniqueCurrency, Long> {
    boolean existsByCurrency(String currencyCode);
}
