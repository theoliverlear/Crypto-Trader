package org.cryptotrader.universal.library.component;

public class SystemScripts {
    public static void blockCurrencyLoading() {
        System.setProperty("cryptotrader.load.currency", "false");
    }

    public static void blockCurrencyHarvesting() {
        System.setProperty("cryptotrader.harvest.currency", "false");
    }
}
