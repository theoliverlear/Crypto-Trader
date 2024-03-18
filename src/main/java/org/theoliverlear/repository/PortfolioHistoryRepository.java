package org.theoliverlear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.theoliverlear.entity.Portfolio;

@Repository
public interface PortfolioHistoryRepository extends JpaRepository<Portfolio, Long> {
}
