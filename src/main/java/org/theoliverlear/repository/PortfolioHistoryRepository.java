package org.theoliverlear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.Portfolio;

public interface PortfolioHistoryRepository extends JpaRepository<Portfolio, Long> {
}
