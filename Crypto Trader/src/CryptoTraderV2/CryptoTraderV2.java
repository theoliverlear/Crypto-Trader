package CryptoTraderV2;

import java.io.IOException;
import java.util.Calendar;

/*
     =============================
    |        Crypto Trader        |
    |         Version 2.0         |
    |      by: Oliver Sigwarth    |
     =============================

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
    public static void main(String[] args) throws IOException, InterruptedException {
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


        PortfolioAsset bitcoinAsset = new PortfolioAsset(bitcoin, bitcoin.getValue(), 1, 0);
        PortfolioAsset shibaAsset = new PortfolioAsset(shiba, shiba.getValue(), 1, 0);
        PortfolioAsset ethereumAsset = new PortfolioAsset(ethereum, ethereum.getValue(), 1, 0);
        PortfolioAsset dogeAsset = new PortfolioAsset(doge, doge.getValue(), 1, 0);
        PortfolioAsset litecoinAsset = new PortfolioAsset(litecoin, litecoin.getValue(), 1, 0);
        PortfolioAsset cardanoAsset = new PortfolioAsset(cardano, cardano.getValue(), 1, 0);
        PortfolioAsset solanaAsset = new PortfolioAsset(solana, solana.getValue(), 1, 0);
        PortfolioAsset polygonAsset = new PortfolioAsset(polygon, polygon.getValue(), 1, 0);
        PortfolioAsset chainlinkAsset = new PortfolioAsset(chainlink, chainlink.getValue(), 1, 0);
        PortfolioAsset stellarLumensAsset = new PortfolioAsset(stellarLumens, stellarLumens.getValue(), 1, 0);
        PortfolioAsset polkadotAsset = new PortfolioAsset(polkadot, polkadot.getValue(), 1, 0);


        PortfolioAssetThread bitcoinAssetThread = new PortfolioAssetThread(bitcoinAsset);
        PortfolioAssetThread shibaAssetThread = new PortfolioAssetThread(shibaAsset);
        PortfolioAssetThread ethereumAssetThread = new PortfolioAssetThread(ethereumAsset);
        PortfolioAssetThread dogeAssetThread = new PortfolioAssetThread(dogeAsset);
        PortfolioAssetThread litecoinAssetThread = new PortfolioAssetThread(litecoinAsset);
        PortfolioAssetThread cardanoAssetThread = new PortfolioAssetThread(cardanoAsset);
        PortfolioAssetThread solanaAssetThread = new PortfolioAssetThread(solanaAsset);
        PortfolioAssetThread polygonAssetThread = new PortfolioAssetThread(polygonAsset);
        PortfolioAssetThread chainlinkAssetThread = new PortfolioAssetThread(chainlinkAsset);
        PortfolioAssetThread stellarLumensAssetThread = new PortfolioAssetThread(stellarLumensAsset);
        PortfolioAssetThread polkadotAssetThread = new PortfolioAssetThread(polkadotAsset);

        Thread BTCThread = new Thread(bitcoinAssetThread);
        Thread SHIBThread = new Thread(shibaAssetThread);
        Thread ETHThread = new Thread(ethereumAssetThread);
        Thread DOGEThread = new Thread(dogeAssetThread);
        Thread LTCThread = new Thread(litecoinAssetThread);
        Thread ADAThread = new Thread(cardanoAssetThread);
        Thread SOLThread = new Thread(solanaAssetThread);
        Thread MATICThread = new Thread(polygonAssetThread);
        Thread LINKThread = new Thread(chainlinkAssetThread);
        Thread XLMThread = new Thread(stellarLumensAssetThread);
        Thread DOTThread = new Thread(polkadotAssetThread);

        BTCThread.start();
        SHIBThread.start();
        ETHThread.start();
        DOGEThread.start();
        LTCThread.start();
        ADAThread.start();
        SOLThread.start();
        MATICThread.start();
        LINKThread.start();
        XLMThread.start();
        DOTThread.start();

    }
}
