package CryptoTraderV2;

import java.io.IOException;

public class CurrencyThread implements Runnable {
    Currency currency;
    public CurrencyThread(Currency currency) {
        this.currency = currency;
    }
    public Currency getCurrency() {
        return this.currency;
    }
    public void startStop() {

    }
    @Override
    public void run() {
        CryptoTraderDatabase cryptoTraderDatabase;
        try {
            cryptoTraderDatabase = new CryptoTraderDatabase(this.getCurrency());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        double previousValue = 0.00;
        do {
            if (previousValue != this.getCurrency().getUpdatedValue()) {
                System.out.println("Thread: " + Thread.currentThread().getName() + " Currency: " + this.getCurrency().getName() + " Updating...");
                cryptoTraderDatabase.updateCurrency();
                previousValue = this.getCurrency().getValue();
            }
        } while (true);
    }
}
