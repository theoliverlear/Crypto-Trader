package CryptoTraderV2;

import java.io.IOException;

public class CurrencyIntervalThread implements Runnable {
    Currency currency;
    public CurrencyIntervalThread(Currency currency) {
        this.currency = currency;
    }
    public Currency getCurrency() {
        return this.currency;
    }
    @Override
    public void run() {
        CryptoTraderDatabase cryptoTraderDatabase;
        try {
            cryptoTraderDatabase = new CryptoTraderDatabase(this.getCurrency());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        do {
            cryptoTraderDatabase.updateCurrencyInterval();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (true);
    }
}
