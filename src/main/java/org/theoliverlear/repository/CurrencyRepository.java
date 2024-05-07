package org.theoliverlear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.theoliverlear.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency getCurrencyByCurrencyCode(String currencyCode);
    @Query("SELECT c FROM Currency c WHERE c.currencyCode = :currencyCode")
    void saveCurrencyByCurrencyCode(@Param("currencyCode") Currency currency);
}
