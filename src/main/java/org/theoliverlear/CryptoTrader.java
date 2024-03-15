package org.theoliverlear;

import org.theoliverlear.entity.Currency;
import org.theoliverlear.model.thread.CurrencyUpdaterThread;

import java.util.Scanner;

public class CryptoTrader {
    public static void main(String[] args) {
        Currency bitcoin = new Currency("Bitcoin", "BTC", "https://api.coinbase.com/v2/prices/BTC-USD/spot");
        CurrencyUpdaterThread currencyUpdaterThread = new CurrencyUpdaterThread(bitcoin);
        currencyUpdaterThread.start();
    }
}
