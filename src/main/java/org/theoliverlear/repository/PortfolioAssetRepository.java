package org.theoliverlear.repository;
//=================================-Imports-==================================
import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.portfolio.PortfolioAsset;

public interface PortfolioAssetRepository extends JpaRepository<PortfolioAsset, Long> {
    //============================-Methods-===================================

    //---------------------Get-Portfolio-Asset-By-Id--------------------------
    PortfolioAsset getPortfolioAssetById(Long id);
    //----------------Get-Portfolio-Asset-By-Currency-Name--------------------
    PortfolioAsset getPortfolioAssetByCurrencyName(String currencyName);
    //----------------------Exists-By-Currency-Name---------------------------
    boolean existsByCurrencyName(String currencyName);
}
