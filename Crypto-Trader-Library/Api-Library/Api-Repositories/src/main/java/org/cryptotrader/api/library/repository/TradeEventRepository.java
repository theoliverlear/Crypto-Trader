package org.cryptotrader.api.library.repository;

import org.cryptotrader.api.library.entity.trade.TradeEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeEventRepository extends JpaRepository<TradeEvent, Long> {
    List<TradeEvent> findAllByAssetHistoryId(Long assetId);
    List<TradeEvent> findAllByPortfolioId(Long portfolioId);
    List<TradeEvent> findAllByPortfolioIdOrderByTradeTimeDesc(Long portfolioId, Pageable pageable);
    boolean existsTradeEventByPortfolioId(Long portfolioId);
}
