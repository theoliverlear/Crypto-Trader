package org.theoliverlear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.theoliverlear.entity.Currency;

public interface CurrencyHistoryRepository extends JpaRepository<Currency, Long> {
//    @Query("SELECT currency FROM currencies WHERE currency.currencyCode = ?1")
//    Currency getCurrencyByCurrencyCode(@Param("currency_code") String currencyCode);


}
