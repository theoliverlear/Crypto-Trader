package org.theoliverlear.repository;
//=================================-Imports-==================================
import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.portfolio.Portfolio;

public interface PortfolioHistoryRepository extends JpaRepository<Portfolio, Long> {
}
