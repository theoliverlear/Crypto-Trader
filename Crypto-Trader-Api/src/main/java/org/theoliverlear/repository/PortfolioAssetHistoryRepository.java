package org.theoliverlear.repository;
//=================================-Imports-==================================
import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.portfolio.PortfolioAssetHistory;

import java.util.List;

public interface PortfolioAssetHistoryRepository extends JpaRepository<PortfolioAssetHistory, Long> {
    //=============================-Methods-==================================

    //------------------Get-First-By-Portfolio-Asset-Id-----------------------
    PortfolioAssetHistory getFirstByPortfolioAssetId(Long portfolioAssetId);
    PortfolioAssetHistory findFirstByPortfolioAssetIdOrderByLastUpdatedDesc(Long portfolioAssetId);
    List<PortfolioAssetHistory> findAllByPortfolioId(Long portfolioId);
}
