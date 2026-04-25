package org.cryptotrader.engine.library.services;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.api.library.entity.portfolio.PortfolioAsset;
import org.cryptotrader.api.library.entity.portfolio.PortfolioAssetHistory;
import org.cryptotrader.api.library.entity.portfolio.PortfolioHistory;
import org.cryptotrader.api.library.entity.trade.TradeEvent;
import org.cryptotrader.api.library.entity.trade.TradeType;
import org.cryptotrader.api.library.entity.user.SubscriptionTier;
import org.cryptotrader.api.library.model.trade.TradingEngine;
import org.cryptotrader.api.library.services.PortfolioService;
import org.cryptotrader.api.library.entity.portfolio.Portfolio;
import org.cryptotrader.api.library.model.trade.CryptoTrader;
import org.cryptotrader.api.library.model.trade.Trader;
import org.cryptotrader.api.library.services.TradeEventService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@ConditionalOnProperty(name = "cryptotrader.engine.trading.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class PortfolioTraderService {
    private final PortfolioService portfolioService;
    private List<Portfolio> allUsersPortfolios;
    private final TradeEventService tradeEventService;
    private final CryptoTrader cryptoTrader;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public PortfolioTraderService(PortfolioService portfolioService,
                                  TradeEventService tradeEventService,
                                  CryptoTrader cryptoTrader,
                                  PlatformTransactionManager transactionManager) {
        this.portfolioService = portfolioService;
        this.tradeEventService = tradeEventService;
        this.cryptoTrader = cryptoTrader;
        this.transactionManager = transactionManager;
    }

    @PostConstruct
    public void init() {
        new TransactionTemplate(this.transactionManager).execute(status -> {
            this.initPortfolios();
            this.initCryptoTrader();
            return null;
        });
    }

    private void initCryptoTrader() {
        for (Portfolio portfolio : this.allUsersPortfolios) {
            this.cryptoTrader.addTrader(new Trader(portfolio));
        }
    }

    private void initPortfolios() {
        this.allUsersPortfolios = this.portfolioService.getAllPortfolios();
        if (this.allUsersPortfolios == null) {
            this.allUsersPortfolios = new ArrayList<>();
        }
    }

    //--------------------------Trade-Portfolios------------------------------
    public void tradePortfolios(List<Portfolio> portfolios, SubscriptionTier subscriptionTier) {
        // TODO: Modularize logging formatting for tiers.
        final String logPrefix = getLogPrefix(subscriptionTier);
        if (portfolios == null || portfolios.isEmpty()) {
            log.info("{} No traders found. No trades will be made.", logPrefix);
            this.fillPortfolioList();
            return;
        }
        log.info("{} Traders found. Trades are being be made.", logPrefix);
        this.triggerAllTraders(this.cryptoTrader.getTradersBySubscriptionTier(subscriptionTier));
    }

    private static @NonNull String getLogPrefix(SubscriptionTier subscriptionTier) {
        final String resetAnsi = "\u001B[0m";
        final String purpleAnsi = "\u001B[35m";
        final String greenAnsi = "\u001B[32m";
        final String blueAnsi = "\u001B[34m";
        final String chosenAnsi = switch (subscriptionTier) {
            case FREE -> greenAnsi;
            case PRO -> blueAnsi;
            case ULTIMATE -> purpleAnsi;
        };
        final String logPrefix = "%s[%s]%s".formatted(chosenAnsi, subscriptionTier.getLabel().toUpperCase(), resetAnsi);
        return logPrefix;
    }

    @Transactional
    @Scheduled(fixedRate = 10_000)
    public void tradeFreePortfolios() {
        this.fillPortfolioList();
        List<Portfolio> portfolios = this.filterBySubscriptionTier(this.allUsersPortfolios, SubscriptionTier.FREE);
        this.tradePortfolios(portfolios, SubscriptionTier.FREE);
    }

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void tradeProPortfolios() {
        this.fillPortfolioList();
        List<Portfolio> portfolios = this.filterBySubscriptionTier(this.allUsersPortfolios, SubscriptionTier.PRO);
        this.tradePortfolios(portfolios, SubscriptionTier.PRO);
    }

    @Transactional
    @Scheduled(fixedRate = 1000)
    public void tradeUltimatePortfolios() {
        this.fillPortfolioList();
        List<Portfolio> portfolios = this.filterBySubscriptionTier(this.allUsersPortfolios, SubscriptionTier.ULTIMATE);
        this.tradePortfolios(portfolios, SubscriptionTier.ULTIMATE);
    }

    public List<Portfolio> filterBySubscriptionTier(List<Portfolio> portfolios, SubscriptionTier tier) {
        List<Portfolio> portfoliosByTier = portfolios.stream()
                .filter(portfolio -> portfolio.getUser().getSubscriptionTier() == tier)
                .toList();
        return portfoliosByTier;
    }

    private void fillPortfolioList() {
        log.debug("Refreshing portfolio list for trading.");
        this.cryptoTrader.getTraders().clear();
        this.allUsersPortfolios = this.portfolioService.getAllPortfolios();
        this.cryptoTrader.addAllPortfolios(this.allUsersPortfolios);
    }

    public void triggerAllTraders() {
        this.triggerAllTraders(this.cryptoTrader.getTraders());
    }

    public void triggerAllTraders(List<Trader> traders) {
        for (Trader trader : traders) {
            for (TradingEngine assetTrader : trader.getAssetTraders()) {
                this.triggerTrader(trader, assetTrader);
            }
        }
    }

    private void triggerTrader(Trader trader, TradingEngine assetTrader) {
        PortfolioAsset traderAsset = assetTrader.getAsset();
        PortfolioAsset previousAsset = PortfolioAsset.from(traderAsset);
        boolean tradeOccurred = assetTrader.trade();
        if (tradeOccurred) {
            final String logPrefix = getLogPrefix(trader.getPortfolio().getUser().getSubscriptionTier());
            log.info("{} Trade executed for trader {} on asset {}. Shares: {} {}, Wallet Dollars: {}, Target Price: {}.",
                     logPrefix,
                     trader.getPortfolio().getUser().getId(),
                     traderAsset.getCurrency().getCurrencyCode(),
                     traderAsset.getShares(),
                     traderAsset.getCurrency().getCurrencyCode(),
                     traderAsset.getAssetWalletDollars(),
                     traderAsset.getTargetPrice());
        }
        updateTraders(trader, assetTrader);
        if (hasAssetChanged(previousAsset, traderAsset)) {
            this.saveAssetChanges(trader, traderAsset, tradeOccurred);
        }
    }

    private void saveAssetChanges(Trader trader, PortfolioAsset traderAsset, boolean tradeOccurred) {
        PortfolioAssetHistory portfolioAssetHistory = new PortfolioAssetHistory(traderAsset, tradeOccurred);
        PortfolioAssetHistory previousPortfolioAssetHistory = this.portfolioService.getLatestPortfolioAssetHistory(traderAsset);
        PortfolioAssetHistory previousWithShares = this.getLastPortfolioAssetWithSharesSinceTime(portfolioAssetHistory);
        this.portfolioService.setPortfolioValueChange(previousPortfolioAssetHistory, portfolioAssetHistory);
        this.portfolioService.setPortfolioSharesChange(previousWithShares, portfolioAssetHistory);
        traderAsset.addPortfolioAssetHistory(portfolioAssetHistory);
        Portfolio traderPortfolio = trader.getPortfolio();
        PortfolioHistory previousPortfolioHistory = this.portfolioService.getLatestPortfolioHistory(traderPortfolio);
        PortfolioHistory portfolioHistory = new PortfolioHistory(traderPortfolio, tradeOccurred);
        this.setValueChange(previousPortfolioHistory, portfolioHistory);
        traderPortfolio.addPortfolioHistory(portfolioHistory);
        this.saveAll(traderAsset, traderPortfolio, portfolioAssetHistory, portfolioHistory);
        if (tradeOccurred) {
            this.saveTradeEvent(portfolioAssetHistory);
        }
    }

    private static boolean hasAssetChanged(PortfolioAsset previousAsset, PortfolioAsset traderAsset) {
        return !previousAsset.equals(traderAsset);
    }

    private static void updateTraders(Trader trader, TradingEngine assetTrader) {
        assetTrader.getAsset().updateValues();
        trader.getPortfolio().updateValues();
    }

    private void saveTradeEvent(PortfolioAssetHistory portfolioAssetHistory) {
        TradeType tradeType = TradeEvent.getTradeType(portfolioAssetHistory);
        TradeEvent tradeEvent = new TradeEvent(portfolioAssetHistory,
                                               tradeType,
                                               portfolioAssetHistory.getValueChange(),
                                               portfolioAssetHistory.getSharesChange());
        this.tradeEventService.saveTradeEvent(tradeEvent);
    }


    private void setValueChange(PortfolioHistory previousPortfolioHistory, PortfolioHistory portfolioHistory) {
        if (previousPortfolioHistory != null) {
            portfolioHistory.calculateValueChange(previousPortfolioHistory);
        } else {
            portfolioHistory.setValueChange(0);
        }
    }

    private void setSharesChange(PortfolioAssetHistory portfolioHistory) {
        PortfolioAssetHistory historyWithShares = getLastPortfolioAssetWithSharesSinceTime(portfolioHistory);
        if (historyWithShares != null) {
            portfolioHistory.calculateShareChange(historyWithShares);
        } else {
            portfolioHistory.setSharesChange(0);
        }
    }

    /**
     * Returns the most recent preceding history entry for the same asset where shares != 0.
     * Used as the baseline for calculating deltas on a new history entry.
     */
    private PortfolioAssetHistory getLastPortfolioAssetWithSharesSinceTime(PortfolioAssetHistory assetToLookRetrospectively) {
        return this.portfolioService.getLatestPreviousAssetHistoryWithShares(assetToLookRetrospectively);
    }

    private void saveAll(PortfolioAsset traderAsset,
                         Portfolio traderPortfolio,
                         PortfolioAssetHistory portfolioAssetHistory,
                         PortfolioHistory portfolioHistory) {
        this.portfolioService.savePortfolioAsset(traderAsset);
        this.portfolioService.savePortfolio(traderPortfolio);
        this.portfolioService.savePortfolioAssetHistory(portfolioAssetHistory);
        this.portfolioService.savePortfolioHistory(portfolioHistory);
    }

    //----------------------Add-Portfolio-To-Traders--------------------------
    public void addPortfolioToTraders(Portfolio portfolio) {
        this.cryptoTrader.addTrader(new Trader(portfolio));
    }
}
