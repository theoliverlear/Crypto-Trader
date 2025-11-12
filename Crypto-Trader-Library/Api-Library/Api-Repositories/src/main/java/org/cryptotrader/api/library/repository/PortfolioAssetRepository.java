package org.cryptotrader.api.library.repository;
//=================================-Imports-==================================

import org.cryptotrader.api.library.entity.portfolio.PortfolioAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


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
    
    List<PortfolioAsset> findAllByPortfolioId(Long portfolioId);
}
