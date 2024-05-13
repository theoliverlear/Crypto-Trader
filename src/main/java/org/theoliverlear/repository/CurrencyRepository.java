package org.theoliverlear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency getCurrencyByCurrencyCode(String currencyCode);
    Currency getCurrencyByName(String currencyName);
}
