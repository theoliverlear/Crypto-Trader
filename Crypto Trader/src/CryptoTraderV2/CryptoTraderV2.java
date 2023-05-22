package CryptoTraderV2;

import java.io.IOException;

/*
     =============================
               [Main] -> [Thread]
                  |          |
                  V          |
            [Trading Algo] <-
                  |
                  V
            [Purchase Log]
                  |
                  V
             [Portfolio] -> (Dollars)
            /     |     \
           V      V      V
    [Currency][Currency][Currency]
         |        |        |
         V        V        V
      (Shares) (Shares) (Shares)
     =============================

        Main
        - Loops (updates data)
            ~ Loop is a thread with defined runnable
            ~ Polls Trading Algo
        - Evaluates if currency is different from previous
            ~ Displays if different
        Trading Algo
            - Detects current value compared to value at previous purchase
            - If value is higher sell, if lower buy
                ~ If you can sell due to lack of funds do nothing
            - Transfer currency shares into Dollars and vice-versa
        Purchase Log
            - How much purchased at what price
            - Time of purchase
        Portfolio
            - Type of Currency ✔
            - Shares of Currency ✔
            - Dollars ✔
            - Has necessary funds or assets ✔
        Currency
            - Current properties ✔
                ~ Name ✔
                ~ Currency Code ✔
                ~ Value ✔
         */
public class CryptoTraderV2 {
    public CryptoTraderV2() {

    }
    public static void main(String[] args) throws IOException {
        Currency bitcoin = new Currency("Bitcoin", "BTC", "https://api.coinbase.com/v2/prices/BTC-USD/spot");
        Currency shiba = new Currency("Shiba Inu", "SHIB", "https://api.coinbase.com/v2/prices/SHIB-USD/spot");
        Currency ethereum = new Currency("Ethereum", "ETH", "https://api.coinbase.com/v2/prices/ETH-USD/spot");
        Currency doge = new Currency("Dogecoin", "DOGE", "https://api.coinbase.com/v2/prices/DOGE-USD/spot");
        Currency litecoin = new Currency("Litecoin", "LTC", "https://api.coinbase.com/v2/prices/LTC-USD/spot");
        Currency cardano = new Currency("Cardano", "ADA", "https://api.coinbase.com/v2/prices/ADA-USD/spot");
        Currency solana = new Currency("Solana", "SOL", "https://api.coinbase.com/v2/prices/SOL-USD/spot");
        Currency polygon = new Currency("Polygon", "MATIC", "https://api.coinbase.com/v2/prices/MATIC-USD/spot");
        Currency chainlink = new Currency("Chainlink", "LINK", "https://api.coinbase.com/v2/prices/LINK-USD/spot");
        Currency stellarLumens = new Currency("Stellar Lumens", "XLM", "https://api.coinbase.com/v2/prices/XLM-USD/spot");
        Currency polkadot = new Currency("Polkadot", "DOT", "https://api.coinbase.com/v2/prices/DOT-USD/spot");

        CurrencyThread bitcoinThread = new CurrencyThread(bitcoin);
        CurrencyThread shibaThread = new CurrencyThread(shiba);
        CurrencyThread ethereumThread = new CurrencyThread(ethereum);
        CurrencyThread dogeThread = new CurrencyThread(doge);
        CurrencyThread litecoinThread = new CurrencyThread(litecoin);
        CurrencyThread cardanoThread = new CurrencyThread(cardano);
        CurrencyThread solanaThread = new CurrencyThread(solana);
        CurrencyThread polygonThread = new CurrencyThread(polygon);
        CurrencyThread chainlinkThread = new CurrencyThread(chainlink);
        CurrencyThread stellarLumensThread = new CurrencyThread(stellarLumens);
        CurrencyThread polkadotThread = new CurrencyThread(polkadot);

        CurrencyIntervalThread bitcoinIntervalThread = new CurrencyIntervalThread(bitcoin);
        CurrencyIntervalThread shibaIntervalThread = new CurrencyIntervalThread(shiba);
        CurrencyIntervalThread ethereumIntervalThread = new CurrencyIntervalThread(ethereum);
        CurrencyIntervalThread dogeIntervalThread = new CurrencyIntervalThread(doge);
        CurrencyIntervalThread litecoinIntervalThread = new CurrencyIntervalThread(litecoin);
        CurrencyIntervalThread cardanoIntervalThread = new CurrencyIntervalThread(cardano);
        CurrencyIntervalThread solanaIntervalThread = new CurrencyIntervalThread(solana);
        CurrencyIntervalThread polygonIntervalThread = new CurrencyIntervalThread(polygon);
        CurrencyIntervalThread chainlinkIntervalThread = new CurrencyIntervalThread(chainlink);
        CurrencyIntervalThread stellarLumensIntervalThread = new CurrencyIntervalThread(stellarLumens);
        CurrencyIntervalThread polkadotIntervalThread = new CurrencyIntervalThread(polkadot);

        Thread BTCThread = new Thread(bitcoinThread);
        Thread SHIBThread = new Thread(shibaThread);
        Thread ETHThread = new Thread(ethereumThread);
        Thread DOGEThread = new Thread(dogeThread);
        Thread LTCThread = new Thread(litecoinThread);
        Thread ADAThread = new Thread(cardanoThread);
        Thread SOLThread = new Thread(solanaThread);
        Thread POLYThread = new Thread(polygonThread);
        Thread LINKThread = new Thread(chainlinkThread);
        Thread XLMThread = new Thread(stellarLumensThread);
        Thread DOTThread = new Thread(polkadotThread);

        Thread BTCIntervalThread = new Thread(bitcoinIntervalThread);
        Thread SHIBIntervalThread = new Thread(shibaIntervalThread);
        Thread ETHIntervalThread = new Thread(ethereumIntervalThread);
        Thread DOGEIntervalThread = new Thread(dogeIntervalThread);
        Thread LTCIntervalThread = new Thread(litecoinIntervalThread);
        Thread ADAIntervalThread = new Thread(cardanoIntervalThread);
        Thread SOLIntervalThread = new Thread(solanaIntervalThread);
        Thread POLYIntervalThread = new Thread(polygonIntervalThread);
        Thread LINKIntervalThread = new Thread(chainlinkIntervalThread);
        Thread XLMIntervalThread = new Thread(stellarLumensIntervalThread);
        Thread DOTIntervalThread = new Thread(polkadotIntervalThread);

        BTCThread.start();
        SHIBThread.start();
        ETHThread.start();
        DOGEThread.start();
        LTCThread.start();
        ADAThread.start();
        SOLThread.start();
        POLYThread.start();
        LINKThread.start();
        XLMThread.start();
        DOTThread.start();

        BTCIntervalThread.start();
        SHIBIntervalThread.start();
        ETHIntervalThread.start();
        DOGEIntervalThread.start();
        LTCIntervalThread.start();
        ADAIntervalThread.start();
        SOLIntervalThread.start();
        POLYIntervalThread.start();
        LINKIntervalThread.start();
        XLMIntervalThread.start();
        DOTIntervalThread.start();
    }
}
