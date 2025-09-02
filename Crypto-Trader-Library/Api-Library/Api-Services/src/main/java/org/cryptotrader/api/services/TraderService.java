package org.cryptotrader.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraderService {
    private final PortfolioService portfolioService;
    
    @Autowired
    public TraderService(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }
}
