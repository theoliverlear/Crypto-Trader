package org.cryptotrader.repository;
//=================================-Imports-==================================

import org.cryptotrader.entity.portfolio.PortfolioAsset;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PortfolioAssetRepository extends JpaRepository<PortfolioAsset, Long> {
    //============================-Methods-===================================

    //---------------------Get-Portfolio-Asset-By-Id--------------------------
    PortfolioAsset getPortfolioAssetById(Long id);
    //----------------Get-Portfolio-Asset-By-Currency-Name--------------------
    PortfolioAsset getPortfolioAssetByCurrencyName(String currencyName);
    //----------------------Exists-By-Currency-Name---------------------------
    boolean existsByCurrencyName(String currencyName);
    //-----------Get-Portfolio-Asset-By-Portfolio-And-Currency----------------
    PortfolioAsset getPortfolioAssetByPortfolioIdAndCurrencyName(Long portfolioId, String currencyName);
}
