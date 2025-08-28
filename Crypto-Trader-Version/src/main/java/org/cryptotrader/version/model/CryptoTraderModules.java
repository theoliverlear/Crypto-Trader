package org.cryptotrader.version.model;

import java.nio.file.Path;
import java.util.Arrays;

public enum CryptoTraderModules {
    CRYPTO_TRADER("Crypto-Trader"),
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
    
    public static CryptoTraderModules resolveFromPath(Path path) {
        return Arrays.stream(CryptoTraderModules.values()).filter(module -> {
            return path.toString().contains(module.getName());
        }).findFirst().orElseThrow(RuntimeException::new);
    }
}
