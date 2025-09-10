package org.cryptotrader.api.library.repository;
//=================================-Imports-==================================

import org.cryptotrader.api.library.entity.currency.CurrencyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CurrencyHistoryRepository extends JpaRepository<CurrencyHistory, Long> {
    boolean existsByCurrencyCurrencyCode(String currencyCode);
    List<CurrencyHistory> findByCurrencyCurrencyCodeAndLastUpdatedAfterOrderByLastUpdatedAsc(String currencyCode, LocalDateTime lastUpdated);

    @Query(value = "SELECT t.bucket_start AS last_updated, ch.currency_value AS value " +
            "FROM generate_series(:since, now(), (:intervalSeconds || ' seconds')::interval) AS t(bucket_start) " +
            "LEFT JOIN LATERAL ( " +
            "    SELECT ch.currency_value, ch.last_updated " +
            "    FROM currency_history ch " +
            "    WHERE ch.currency_code = :code " +
            "      AND ch.last_updated >= t.bucket_start " +
            "      AND ch.last_updated < t.bucket_start + (:intervalSeconds || ' seconds')::interval " +
            "    ORDER BY ch.last_updated ASC " +
            "    LIMIT 1 " +
            ") ch ON true " +
            "WHERE ch.currency_value IS NOT NULL " +
            "ORDER BY t.bucket_start ASC", nativeQuery = true)
    List<Object[]> findDownsampledHistory(@Param("code") String currencyCode,
                                          @Param("since") LocalDateTime since,
                                          @Param("intervalSeconds") int intervalSeconds);
}
