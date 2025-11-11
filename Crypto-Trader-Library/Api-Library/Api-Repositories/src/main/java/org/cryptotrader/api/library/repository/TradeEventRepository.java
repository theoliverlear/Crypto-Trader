package org.cryptotrader.api.library.repository;

import org.cryptotrader.api.library.entity.trade.TradeEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeEventRepository extends JpaRepository<TradeEvent, Long> {
    
}
