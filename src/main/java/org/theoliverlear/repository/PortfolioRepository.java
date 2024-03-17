package org.theoliverlear.repository;
//=================================-Imports-==================================
import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    //=============================-Methods-==================================
    Portfolio findPortfolioByUserId(Long userId);
}
