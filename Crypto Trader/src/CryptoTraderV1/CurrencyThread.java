package CryptoTraderV1;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyThread implements Runnable {
    Currency currency;
    Wallet wallet;

    public CurrencyThread(Currency currency, Wallet wallet) {
        this.currency = currency;
        this.wallet = wallet;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Wallet getWallet() {
        return this.wallet;
    }

    @Override
    public void run() {
        Pattern timePattern = Pattern.compile("..:..:..");
        double previousValue = 0.00;
        do {
            if (previousValue != this.getCurrency().getUpdatedValue()) {
                String time = Calendar.getInstance().getTime().toString();
                Matcher timeMatcher = timePattern.matcher(time);
                while (timeMatcher.find()) {
                    System.out.print(timeMatcher.group() + " - ");
                    break;
                }

                System.out.println(this.getCurrency().toString());
                previousValue = this.getCurrency().getUpdatedValue();
            }
        } while (true);
    }
}
