package org.cryptotrader.version.model;

public enum CryptoTraderModules {
    ADMIN("Crypto-Trader-Admin"),
    ANALYSIS("Crypto-Trader-Analysis"),
    API("Crypto-Trader-Api"),
    ASSETS("Crypto-Trader-Assets"),
    LIBRARY("Crypto-Trader-Library"),
    API_LIBRARY("Api-Library"),
    API_COMMUNICATION("Api-Communication"),
    API_COMPONENTS("Api-Components"),
    API_MODELS("Api-Models"),
    API_REPOSITORIES("Api-Repositories"),
    DESKTOP_LIBRARY("Desktop-Library"),
    PROMO("Crypto-Trader-Promo"),
    TESTING("Crypto-Trader-Testing"),
    VERSION("Crypto-Trader-Version"),
    WEBSITE("Crypto-Trader-Website");
    
    private final String name;
    
    CryptoTraderModules(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
