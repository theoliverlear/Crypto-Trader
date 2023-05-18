package CryptoTraderV1;

import java.util.Calendar;
import java.util.TreeMap;

public class CurrencyList {
    Currency currency;
    TreeMap<Calendar, Currency> calendarCurrencyTreeMap;
    public CurrencyList(Currency currency) throws InterruptedException {
        this.currency = currency;
        this.calendarCurrencyTreeMap = new TreeMap<>();
        for (int i = 0; i < 180; i++) {
            this.calendarCurrencyTreeMap.put(Calendar.getInstance(), this.currency);
            Thread.sleep(1000);
        }
    }

    public TreeMap<Calendar, Currency> getCalendarCurrencyTreeMap() {
        return this.calendarCurrencyTreeMap;
    }
}
