package org.theoliverlear.repository;
//=================================-Imports-==================================
import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.portfolio.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    //=============================-Methods-==================================

    //---------------------Find-Portfolio-By-User-Id--------------------------
    Portfolio findPortfolioByUserId(Long userId);
}
