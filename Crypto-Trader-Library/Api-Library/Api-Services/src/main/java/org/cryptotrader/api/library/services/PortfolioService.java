package org.cryptotrader.api.library.services;
//=================================-Imports-==================================

import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.api.library.communication.request.PortfolioAssetRequest;
import org.cryptotrader.api.library.communication.response.PortfolioAssetHistoryResponse;
import org.cryptotrader.api.library.communication.response.PortfolioHistoryResponse;
import org.cryptotrader.api.library.entity.currency.Currency;
import org.cryptotrader.api.library.entity.portfolio.Portfolio;
import org.cryptotrader.api.library.entity.portfolio.PortfolioAsset;
import org.cryptotrader.api.library.entity.portfolio.PortfolioAssetHistory;
import org.cryptotrader.api.library.entity.portfolio.PortfolioHistory;
import org.cryptotrader.api.library.repository.PortfolioAssetHistoryRepository;
import org.cryptotrader.api.library.repository.PortfolioAssetRepository;
import org.cryptotrader.api.library.repository.PortfolioHistoryRepository;
import org.cryptotrader.api.library.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PortfolioService {
    //============================-Variables-=================================
    private final PortfolioRepository portfolioRepository;
    private final PortfolioAssetRepository portfolioAssetRepository;
    private final PortfolioHistoryRepository portfolioHistoryRepository;
    private final PortfolioAssetHistoryRepository portfolioAssetHistoryRepository;
    private final CurrencyService currencyService;
    //===========================-Constructors-===============================
    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository,
                            PortfolioAssetRepository portfolioAssetRepository,
                            PortfolioHistoryRepository portfolioHistoryRepository,
                            PortfolioAssetHistoryRepository portfolioAssetHistoryRepository,
                            CurrencyService currencyService) {
        this.currencyService = currencyService;
        this.portfolioRepository = portfolioRepository;
        this.portfolioAssetRepository = portfolioAssetRepository;
        this.portfolioHistoryRepository = portfolioHistoryRepository;
        this.portfolioAssetHistoryRepository = portfolioAssetHistoryRepository;
    }
    //============================-Methods-===================================


    public void setPortfolioValueChange(PortfolioAssetHistory previousPortfolioAssetHistory,
                                        PortfolioAssetHistory portfolioAssetHistory) {
        if (previousPortfolioAssetHistory != null) {
            portfolioAssetHistory.calculateValueChange(previousPortfolioAssetHistory);
        } else {
            portfolioAssetHistory.setValueChange(0);
        }
    }

    public PortfolioAssetHistoryResponse toPortfolioAssetHistoryResponse(PortfolioAssetHistory portfolioAssetHistory) {
        return new PortfolioAssetHistoryResponse(
                portfolioAssetHistory.getCurrency().getCurrencyCode(),
                portfolioAssetHistory.getShares(),
                portfolioAssetHistory.getSharesValueInDollars(),
                portfolioAssetHistory.getAssetWalletDollars(),
                portfolioAssetHistory.getTargetPrice(),
                portfolioAssetHistory.getValueChange(),
                portfolioAssetHistory.getSharesChange(),
                portfolioAssetHistory.isTradeOccurred(),
                portfolioAssetHistory.getVendor().getName(),
                portfolioAssetHistory.getVendor().getRate(),
                portfolioAssetHistory.getLastUpdated()
        );
    }
    
    public PortfolioHistoryResponse toPortfolioHistoryResponse(PortfolioHistory portfolioHistory) {
        return new PortfolioHistoryResponse(
                portfolioHistory.getDollarBalance(),
                portfolioHistory.getShareBalance(),
                portfolioHistory.getTotalWorth(),
                portfolioHistory.getValueChange(),
                portfolioHistory.isTradeOccurred(),
                portfolioHistory.getLastUpdated()
        );
    }
    
    public List<PortfolioAssetHistoryResponse> toHistoryResponses(List<PortfolioAssetHistory> assetHistories) {
        return assetHistories.stream()
                .map(this::toPortfolioAssetHistoryResponse)
                .toList();
    }
    
    public List<PortfolioAssetHistory> getPortfolioAssetHistory(Portfolio portfolio) {
        return this.portfolioAssetHistoryRepository.findAllByPortfolioId(portfolio.getId());
    }
    public PortfolioAssetHistory getLatestPortfolioAssetHistory(PortfolioAsset portfolioAsset) {
        return this.portfolioAssetHistoryRepository.findFirstByPortfolioAssetIdOrderByLastUpdatedDesc(portfolioAsset.getId());
    }
    public PortfolioHistory getLatestPortfolioHistory(Portfolio portfolio) {
        return this.portfolioHistoryRepository.findFirstByPortfolioIdOrderByLastUpdatedDesc(portfolio.getId());
    }
    //---------------------Save-Portfolio-Asset-History-----------------------
    public void savePortfolioAssetHistory(PortfolioAssetHistory portfolioAssetHistory) {
        this.portfolioAssetHistoryRepository.save(portfolioAssetHistory);
    }
    //----------------------Save-Portfolio-History----------------------------
    public void savePortfolioHistory(PortfolioHistory portfolioHistory) {
        this.portfolioHistoryRepository.save(portfolioHistory);
    }

    //---------------------------Save-Portfolio-------------------------------
    public void savePortfolio(Portfolio portfolio) {
        this.portfolioRepository.save(portfolio);
    }
    //------------------------Save-Portfolio-Asset----------------------------
    public void savePortfolioAsset(PortfolioAsset portfolioAsset) {
        this.portfolioAssetRepository.save(portfolioAsset);
    }
    //----------------------Get-Portfolio-By-User-Id--------------------------
    public Portfolio getPortfolioByUserId(Long userId) {
        return this.portfolioRepository.findPortfolioByUserId(userId);
    }
    //-------------------------Get-All-Portfolios-----------------------------
    public List<Portfolio> getAllPortfolios() {
        return this.portfolioRepository.findAll();
    }
    //-----------------------Add-Asset-To-Portfolio---------------------------
    public void addAssetToPortfolio(Portfolio portfolio, PortfolioAssetRequest portfolioAssetRequest) {
        Currency requestCurrency = this.currencyService.getCurrencyByName(portfolioAssetRequest.getCurrencyName());
        PortfolioAsset portfolioAsset = new PortfolioAsset(portfolio, requestCurrency, portfolioAssetRequest.getShares(), portfolioAssetRequest.getWalletDollars());
        this.savePortfolio(portfolio);
        this.savePortfolioAsset(portfolioAsset);
    }
    //-----------------------Get-Portfolio-History----------------------------
    public List<PortfolioHistory> getPortfolioHistory(Portfolio portfolio) {
        return this.portfolioHistoryRepository.findAllByPortfolioId(portfolio.getId());
    }
    
    //------------------------Get-Portfolio-Profit----------------------------
    public double getPortfolioProfit(Portfolio portfolio) {
        PortfolioHistory initialPortfolioHistory = this.portfolioHistoryRepository.getFirstByPortfolioId(portfolio.getId());
        if (initialPortfolioHistory == null) {
            return 0;
        }
        double profit = portfolio.getTotalWorth() - initialPortfolioHistory.getTotalWorth();
        return profit;
    }
    //---------------------Get-Portfolio-Asset-Profit-------------------------
    public double getPortfolioAssetProfit(PortfolioAsset portfolioAsset) {
        PortfolioAssetHistory initialPortfolioAssetHistory = this.portfolioAssetHistoryRepository.getFirstByPortfolioAssetId(portfolioAsset.getId());
        if (initialPortfolioAssetHistory == null) {
            return 0;
        }
        double profit = portfolioAsset.getTotalValueInDollars() - initialPortfolioAssetHistory.getTotalValueInDollars();
        return profit;
    }
    //----------------Get-Portfolio-Asset-By-Currency-Name--------------------
    public PortfolioAsset getPortfolioAssetByCurrencyName(Portfolio portfolio, String currencyName) {
        return this.portfolioAssetRepository.getPortfolioAssetByPortfolioIdAndCurrencyName(portfolio.getId(), currencyName);
    }
}