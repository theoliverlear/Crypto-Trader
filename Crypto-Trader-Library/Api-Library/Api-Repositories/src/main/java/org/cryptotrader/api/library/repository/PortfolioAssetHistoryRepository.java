package org.cryptotrader.api.library.repository;
//=================================-Imports-==================================

import org.cryptotrader.api.library.entity.portfolio.PortfolioAssetHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PortfolioAssetHistoryRepository extends JpaRepository<PortfolioAssetHistory, Long> {
    //=============================-Methods-==================================

    //------------------Get-First-By-Portfolio-Asset-Id-----------------------
    PortfolioAssetHistory getFirstByPortfolioAssetId(Long portfolioAssetId);
    PortfolioAssetHistory findFirstByPortfolioAssetIdOrderByLastUpdatedDesc(Long portfolioAssetId);
    List<PortfolioAssetHistory> findAllByPortfolioId(Long portfolioId);

    //------------------Find-Previous-With-Shares-Before-Time-----------------
    @Query("SELECT h FROM PortfolioAssetHistory h " +
           "WHERE h.portfolioAsset.id = :portfolioAssetId " +
           "AND h.lastUpdated <= :before " +
           "AND h.shares <> 0 " +
           "ORDER BY h.lastUpdated DESC")
    List<PortfolioAssetHistory> findLatestWithSharesBefore(@Param("portfolioAssetId") Long portfolioAssetId,
                                                           @Param("before") LocalDateTime before);
}
