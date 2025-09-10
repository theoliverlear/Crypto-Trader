package org.cryptotrader.api.library.repository;
//=================================-Imports-==================================

import org.cryptotrader.api.library.entity.portfolio.PortfolioHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioHistoryRepository extends JpaRepository<PortfolioHistory, Long> {
    //=============================-Methods-==================================

    //----------------------Find-All-By-Portfolio-Id--------------------------
    List<PortfolioHistory> findAllByPortfolioId(Long portfolioId);
    //---------------------Get-First-By-Portfolio-Id--------------------------
    PortfolioHistory getFirstByPortfolioId(Long portfolioId);
    PortfolioHistory findFirstByPortfolioIdOrderByLastUpdatedDesc(Long portfolioId);
}
