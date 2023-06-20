package CryptoTraderV2;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

/*
     =============================
    |        Crypto Trader        |
    |         Version 2.0         |
    |      by: Oliver Sigwarth    |
     =============================

     =============================
           [CryptoTraderV2]
                  |
                  V
         [PortfolioAssetThread]
                  |
                  V
           [PortfolioAsset]
                  |
                  V
         <PortfolioAsset.poll()>
             |          |
             V          V
          (dollars)  (shares)
     =============================

     =============================
         [CryptoTraderDatabase]
                  |
                  V
            [CurrencyThread]
                  |
                  V
           [PortfolioAsset]
                  |
                  V
         <PortfolioAsset.poll()>
             |          |
             V          V
          (dollars)  (shares)
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
    public static void main(String[] args) throws IOException, InterruptedException, SQLException {
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

        CryptoTraderDatabase bitcoinDatabase = new CryptoTraderDatabase(bitcoin);
        CryptoTraderDatabase shibaDatabase = new CryptoTraderDatabase(shiba);
        CryptoTraderDatabase ethereumDatabase = new CryptoTraderDatabase(ethereum);
        CryptoTraderDatabase dogeDatabase = new CryptoTraderDatabase(doge);
        CryptoTraderDatabase litecoinDatabase = new CryptoTraderDatabase(litecoin);
        CryptoTraderDatabase cardanoDatabase = new CryptoTraderDatabase(cardano);
        CryptoTraderDatabase solanaDatabase = new CryptoTraderDatabase(solana);
        CryptoTraderDatabase polygonDatabase = new CryptoTraderDatabase(polygon);
        CryptoTraderDatabase chainlinkDatabase = new CryptoTraderDatabase(chainlink);
        CryptoTraderDatabase stellarLumensDatabase = new CryptoTraderDatabase(stellarLumens);
        CryptoTraderDatabase polkadotDatabase = new CryptoTraderDatabase(polkadot);

        PortfolioAsset bitcoinAsset = new PortfolioAsset(bitcoin, bitcoinDatabase.getValueFromPortfolioDatabase("Portfolio", "target_price"), bitcoinDatabase.getValueFromPortfolioDatabase("Portfolio","shares"), bitcoinDatabase.getValueFromPortfolioDatabase("Portfolio","dollars"));
        PortfolioAsset shibaAsset = new PortfolioAsset(shiba, shibaDatabase.getValueFromPortfolioDatabase("Portfolio", "target_price"), shibaDatabase.getValueFromPortfolioDatabase("Portfolio","shares"), shibaDatabase.getValueFromPortfolioDatabase("Portfolio","dollars"));
        PortfolioAsset ethereumAsset = new PortfolioAsset(ethereum, ethereumDatabase.getValueFromPortfolioDatabase("Portfolio", "target_price"), ethereumDatabase.getValueFromPortfolioDatabase("Portfolio","shares"), ethereumDatabase.getValueFromPortfolioDatabase("Portfolio","dollars"));
        PortfolioAsset dogeAsset = new PortfolioAsset(doge, dogeDatabase.getValueFromPortfolioDatabase("Portfolio", "target_price"), dogeDatabase.getValueFromPortfolioDatabase("Portfolio","shares"), dogeDatabase.getValueFromPortfolioDatabase("Portfolio","dollars"));
        PortfolioAsset litecoinAsset = new PortfolioAsset(litecoin, litecoinDatabase.getValueFromPortfolioDatabase("Portfolio", "target_price"), litecoinDatabase.getValueFromPortfolioDatabase("Portfolio","shares"), litecoinDatabase.getValueFromPortfolioDatabase("Portfolio","dollars"));
        PortfolioAsset cardanoAsset = new PortfolioAsset(cardano, cardanoDatabase.getValueFromPortfolioDatabase("Portfolio", "target_price"), cardanoDatabase.getValueFromPortfolioDatabase("Portfolio","shares"), cardanoDatabase.getValueFromPortfolioDatabase("Portfolio","dollars"));
        PortfolioAsset solanaAsset = new PortfolioAsset(solana, solanaDatabase.getValueFromPortfolioDatabase("Portfolio", "target_price"), solanaDatabase.getValueFromPortfolioDatabase("Portfolio","shares"), solanaDatabase.getValueFromPortfolioDatabase("Portfolio","dollars"));
        PortfolioAsset polygonAsset = new PortfolioAsset(polygon, polygonDatabase.getValueFromPortfolioDatabase("Portfolio", "target_price"), polygonDatabase.getValueFromPortfolioDatabase("Portfolio","shares"), polygonDatabase.getValueFromPortfolioDatabase("Portfolio","dollars"));
        PortfolioAsset chainlinkAsset = new PortfolioAsset(chainlink, chainlinkDatabase.getValueFromPortfolioDatabase("Portfolio", "target_price"), chainlinkDatabase.getValueFromPortfolioDatabase("Portfolio","shares"), chainlinkDatabase.getValueFromPortfolioDatabase("Portfolio","dollars"));
        PortfolioAsset stellarLumensAsset = new PortfolioAsset(stellarLumens, stellarLumensDatabase.getValueFromPortfolioDatabase("Portfolio", "target_price"), stellarLumensDatabase.getValueFromPortfolioDatabase("Portfolio","shares"), stellarLumensDatabase.getValueFromPortfolioDatabase("Portfolio","dollars"));
        PortfolioAsset polkadotAsset = new PortfolioAsset(polkadot, polkadotDatabase.getValueFromPortfolioDatabase("Portfolio", "target_price"), polkadotDatabase.getValueFromPortfolioDatabase("Portfolio","shares"), polkadotDatabase.getValueFromPortfolioDatabase("Portfolio","dollars"));

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
