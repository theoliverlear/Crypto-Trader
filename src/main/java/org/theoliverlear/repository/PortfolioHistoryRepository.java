package org.theoliverlear.repository;
//=================================-Imports-==================================
import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.portfolio.PortfolioHistory;

public interface PortfolioHistoryRepository extends JpaRepository<PortfolioHistory, Long> {
}
