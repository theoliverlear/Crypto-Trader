package CryptoTraderV2;

import java.util.Map;
import java.util.TreeMap;
import CryptoTraderV2.Currency;
import static CryptoTraderV2.Currency.isSameDenominationStatic;
public class Portfolio {
    Currency currency;
    double dollars;
    TreeMap<Currency, Double> currencyShares;
    TreeMap<Double, TreeMap<Currency, Double>> dollarPortfolio;
    public Portfolio(double dollars) {
        this.dollars = dollars;
        this.currencyShares = new TreeMap<>();
        this.dollarPortfolio = new TreeMap<>();
    }
    public void addAsset(Currency currency, double shares) {
        this.currencyShares.put(currency, shares);
    }

    public void increaseAsset(Currency currency, double amount) {
        if (this.containsCurrency(currency)) {
            for (Map.Entry<Currency, Double> portfolioEntry : this.currencyShares.entrySet()) {
                if (isSameDenominationStatic(currency, portfolioEntry.getKey())) {
                    this.currencyShares.replace(currency, portfolioEntry.getValue(), portfolioEntry.getValue() + amount);
                }
            }
        }
    }

    public void decreaseAsset(Currency currency, double amount) {
        for (Map.Entry<Currency, Double> portfolioEntry : this.currencyShares.entrySet()) {
            if (isSameDenominationStatic(currency, portfolioEntry.getKey())) {
                this.currencyShares.replace(currency, portfolioEntry.getValue(),portfolioEntry.getValue() + amount);
            }
        }
    }

    public double dollarsToShares(Currency currency, double dollars) {
        return dollars * currency.getValue();
    }
    public double sharesToDollars(Currency currency, double shares) {
        return shares * currency.getValue();
    }
    public boolean containsCurrency(Currency currency) {
        for (Map.Entry<Currency, Double> portfolioEntry : this.currencyShares.entrySet()) {
            if (isSameDenominationStatic(currency, portfolioEntry.getKey())){
                return true;
            }
        }
        System.out.println("Your portfolio does not contain " + currency.getName() + ".");
        return false;
    }
    public double getSharesFromCurrency(Currency currency) {
        return this.currencyShares.get(currency);
    }
    public double getDollars() {
        return this.dollars;
    }
    public void setDollars(double dollars) {
        this.dollars = dollars;
    }
    public void addDollars(double amount) {
        this.dollars += amount;
    }
    public void subtractDollars(double amount) {
        if (hasEnoughDollars(amount)) {
            this.dollars -= amount;
        } else {
            System.out.println("You do not have enough dollars to withdraw " + amount + " dollars.");
        }
    }
    public boolean hasEnoughDollars(double amount) {
        return amount <= this.dollars;
    }
    public boolean hasEnoughShares(Currency currency, double amount) {
        if (this.containsCurrency(currency)) {
            if (this.currencyShares.get(currency) - amount >= 0) {
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("You do not have enough shares to withdraw " + amount + " shares.");
            return false;
        }
    }

}
