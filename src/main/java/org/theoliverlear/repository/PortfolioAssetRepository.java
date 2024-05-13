package org.theoliverlear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.PortfolioAsset;

public interface PortfolioAssetRepository extends JpaRepository<PortfolioAsset, Long> {
    PortfolioAsset getPortfolioAssetById(Long id);
    PortfolioAsset getPortfolioAssetByCurrencyName(String currencyName);
    boolean existsByCurrencyName(String currencyName);
}
