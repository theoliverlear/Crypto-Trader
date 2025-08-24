package org.cryptotrader.repository;
//=================================-Imports-==================================

import org.cryptotrader.entity.portfolio.PortfolioAssetHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioAssetHistoryRepository extends JpaRepository<PortfolioAssetHistory, Long> {
    //=============================-Methods-==================================

    //------------------Get-First-By-Portfolio-Asset-Id-----------------------
    PortfolioAssetHistory getFirstByPortfolioAssetId(Long portfolioAssetId);
    PortfolioAssetHistory findFirstByPortfolioAssetIdOrderByLastUpdatedDesc(Long portfolioAssetId);
    List<PortfolioAssetHistory> findAllByPortfolioId(Long portfolioId);
}
