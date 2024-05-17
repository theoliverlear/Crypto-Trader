package org.theoliverlear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.portfolio.PortfolioAssetHistory;

public interface PortfolioAssetHistoryRepository extends JpaRepository<PortfolioAssetHistory, Long> {

}
