package CryptoTraderV1;

public class CryptoTrader {
    Wallet wallet;
    public CryptoTrader() {

    }
    public void buySellNone(Currency currency) {
        System.out.println(this.wallet.getShares(currency));
    }
    public void triggerSell() {

    }
    public void startCryptoTrader() {
        this.wallet = new Wallet(500.00);
        Currency bitcoin = new Currency("Bitcoin", "BTC", "https://api.coinbase.com/v2/prices/BTC-USD/spot");
        Currency shibaInu = new Currency("Shiba-Inu", "SHIB", "https://api.coinbase.com/v2/prices/SHIB-USD/spot");
        CurrencyThread bitcoinRunnable = new CurrencyThread(bitcoin, this.wallet);
        Thread threadBTC = new Thread(bitcoinRunnable);
        CurrencyThread shibaInuRunnable = new CurrencyThread(shibaInu, this.wallet);
        Thread threadSHIB = new Thread(shibaInuRunnable);
        threadBTC.start();
        threadSHIB.start();


    }
    public static void main(String[] args) {
        CryptoTrader cryptoTrader = new CryptoTrader();
        cryptoTrader.startCryptoTrader();

    }
}