package org.cryptotrader.api.library.services;

import org.cryptotrader.api.library.communication.response.TradeEventListResponse;
import org.cryptotrader.api.library.communication.response.TradeEventResponse;
import org.cryptotrader.api.library.entity.portfolio.PortfolioAsset;
import org.cryptotrader.api.library.entity.trade.TradeEvent;
import org.cryptotrader.api.library.entity.user.ProductUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TraderService {
    private final PortfolioService portfolioService;
    private final CurrencyService currencyService;
    private final TradeEventService tradeEventService;

    @Autowired
    public TraderService(PortfolioService portfolioService,
                         CurrencyService currencyService,
                         TradeEventService tradeEventService) {
        this.portfolioService = portfolioService;
        this.currencyService = currencyService;
        this.tradeEventService = tradeEventService;
    }

    public TradeEventListResponse getTradeEvents(ProductUser user) {
        List<TradeEvent> tradeEvents = this.getAllTradeEvents(user);
        return this.toTradeEventListResponse(tradeEvents);
    }
    public List<TradeEvent> getAllTradeEvents(ProductUser user) {
        return this.tradeEventService.getAllByProductUser(user);
    }

    public TradeEventListResponse toTradeEventListResponse(List<TradeEvent> tradeEvents) {
        return new TradeEventListResponse(tradeEvents.stream()
                                                     .map(this::toTradeEventResponse)
                                                     .toList()
        );
    }

    public TradeEventResponse toTradeEventResponse(TradeEvent tradeEvent) {
        return new TradeEventResponse(
                this.currencyService.getCurrencyName(true, 
                        tradeEvent.getAssetHistory().getCurrency()), 
                        tradeEvent.getValueChange(),
                        tradeEvent.getSharesChange(),
                        tradeEvent.getTradeTime(),
                        tradeEvent.getTradeType().getName()
        );
    }
}
