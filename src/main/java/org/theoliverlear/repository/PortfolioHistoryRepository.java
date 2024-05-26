package org.theoliverlear.repository;
//=================================-Imports-==================================
import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.portfolio.PortfolioHistory;

import java.util.List;

public interface PortfolioHistoryRepository extends JpaRepository<PortfolioHistory, Long> {
    //=============================-Methods-==================================

    //----------------------Find-All-By-Portfolio-Id--------------------------
    List<PortfolioHistory> findAllByPortfolioId(Long portfolioId);
    //---------------------Get-First-By-Portfolio-Id--------------------------
    PortfolioHistory getFirstByPortfolioId(Long portfolioId);
}
