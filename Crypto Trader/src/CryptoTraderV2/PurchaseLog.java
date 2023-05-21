package CryptoTraderV2;

import java.util.Calendar;

/*
Purchase Log
    - Time Object
    - Currency
        ~ Buy/Sell
        ~ Amount
        ~ Price
 */
public class PurchaseLog {
    Calendar purchaseTime;
    Currency currency;
    double amount;
    String buyOrSell;
    public PurchaseLog(Currency currency, double amount, String buyOrSell) {
        this.purchaseTime = Calendar.getInstance();
        this.currency = currency;
        this.amount = amount;
        this.buyOrSell = buyOrSell;
    }
    public void executeTrade() {

    }
}
