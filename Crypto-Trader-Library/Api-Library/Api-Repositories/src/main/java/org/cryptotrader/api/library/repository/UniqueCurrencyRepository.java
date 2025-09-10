package org.cryptotrader.api.library.repository;

import org.cryptotrader.api.library.entity.currency.UniqueCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniqueCurrencyRepository extends JpaRepository<UniqueCurrency, Long> {
    boolean existsByCurrency(String currencyCode);
}
