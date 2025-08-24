package org.cryptotrader.repository;
//=================================-Imports-==================================

import org.cryptotrader.entity.portfolio.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    //=============================-Methods-==================================

    //---------------------Find-Portfolio-By-User-Id--------------------------
    Portfolio findPortfolioByUserId(Long userId);
}
