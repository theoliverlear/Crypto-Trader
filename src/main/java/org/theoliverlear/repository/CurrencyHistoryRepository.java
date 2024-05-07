package org.theoliverlear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.theoliverlear.entity.Currency;
import org.theoliverlear.entity.CurrencyHistory;

public interface CurrencyHistoryRepository extends JpaRepository<CurrencyHistory, Long> {

}
