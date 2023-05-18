package CryptoTraderV1;

import java.util.TreeMap;

public class Wallet {
    double shares;
    double USD;
    TreeMap<Currency, Double> currencySharesMap;
    TreeMap<Currency, Double> sharesToUSDMap;
    public Wallet(double USD) {
        this.currencySharesMap = new TreeMap<>();
        this.sharesToUSDMap = new TreeMap<>();
        this.USD = USD;
    }
    public double getSharesToUSD(Currency currency) {
        return this.getShares(currency) * currency.getValue();
    }
    public TreeMap<Currency, Double> getCurrencySharesMap() {
        return this.currencySharesMap;
    }
    public void addCurrency(Currency currency, double shares) {
        this.currencySharesMap.put(currency, shares);
        this.sharesToUSDMap.put(currency, this.getSharesToUSD(currency));
    }
    public void addUSD(double amount) {
        this.USD += amount;
    }
    public void removeUSD(double amount) {
        this.USD -= amount;
    }
    public double getShares(Currency currency) {
        return this.currencySharesMap.get(currency);
    }
    public void addCurrencyShares(Currency currency, double amountShares) {
        this.currencySharesMap.replace(currency, this.getShares(currency) + amountShares);
    }
    public void removeCurrencyShares(Currency currency, double amountShares) {
        if (amountShares <= this.getShares(currency)) {
            this.currencySharesMap.replace(currency, this.getShares(currency) - amountShares);
        }
    }
}
