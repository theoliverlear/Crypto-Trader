package org.cryptotrader.version.model.module;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

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
    DESKTOP_COMPONENTS("Desktop-Components"),
    VERSION_LIBRARY("Version-Library"),
    VERSION_MODELS("Version-Models"),
    EXTERNALS_LIBRARY("Externals-Library"),
    EXTERNALS_OPENAI("Externals-OpenAI"),
    PROMO("Crypto-Trader-Promo"),
    TESTING("Crypto-Trader-Testing"),
    VERSION("Crypto-Trader-Version"),
    WEBSITE("Crypto-Trader-Website"),
    CRYPTO_TRADER("Crypto-Trader");

    private final String name;
    
    CryptoTraderModules(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static CryptoTraderModules resolveFromPath(Path path) {
        String pathText = path.toString();
        String[] segments = pathText.split("[\\\\/]+");
        CryptoTraderModules deepestSegmentMatch = null;
        int deepestIndex = -1;
        for (int i = 0; i < segments.length; i++) {
            String segment = segments[i];
            for (CryptoTraderModules module : CryptoTraderModules.values()) {
                if (segment.equals(module.getName())) {
                    if (i > deepestIndex) {
                        deepestIndex = i;
                        deepestSegmentMatch = module;
                    }
                }
            }
        }
        if (deepestSegmentMatch != null) {
            return deepestSegmentMatch;
        }
        return Arrays.stream(CryptoTraderModules.values())
                .filter(module -> pathText.contains(module.getName()))
                .max(Comparator.comparingInt(module -> module.getName().length()))
                .orElseThrow(RuntimeException::new);
    }
}
