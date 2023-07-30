package CryptoTraderV2;
import java.util.Map;
import java.util.TreeMap;

import static CryptoTraderV2.Currency.isSameDenominationStatic;
/*
Portfolio
    - Dollars
        ~ Add ✔
        ~ Subtract ✔
        ~ Get ✔
        ~ Set ✔
        ~ hasEnough ✔
    - currencyShares
        ~ Add Asset ✔
        ~ Increase Shares ✔
        ~ Decrease Shares
        ~ Get Shares Amount ✔
        ~ Has Enough Shares ✔
        ~ Contains Currency ✔
    - Utilities
        ~ Dollars to Shares ✔
        ~ Shares to Dollars ✔
 */

public class Portfolio {
    double dollars;
    TreeMap<Currency, Double> currencyShares;

    public Portfolio(double dollars) {
        this.dollars = dollars;
        this.currencyShares = new TreeMap<>();
    }
    //==========================-Dollar-Methods-==============================
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
    //=====================-Currency-Shares-Methods-==========================
    public void addAsset(Currency currency, double shares) {
        this.currencyShares.put(currency, shares);
    }

    public void increaseShares(Currency currency, double amount) {
        if (this.containsCurrency(currency)) {
            for (Map.Entry<Currency, Double> portfolioEntry : this.currencyShares.entrySet()) {
                if (isSameDenominationStatic(currency, portfolioEntry.getKey())) {
                    this.currencyShares.replace(currency, portfolioEntry.getValue(),
                                       portfolioEntry.getValue() + amount);
                }
            }
        } else {
            System.out.println("You do not have any " + currency.getName() + " to deposit into.");
        }
    }

    public void decreaseDecrease(Currency currency, double amount) {
        if (this.containsCurrency(currency)) {
            for (Map.Entry<Currency, Double> portfolioEntry : this.currencyShares.entrySet()) {
                if (isSameDenominationStatic(currency, portfolioEntry.getKey())) {

                    this.currencyShares.replace(currency, portfolioEntry.getValue(),
                                       portfolioEntry.getValue() + amount);
                }
            }
        } else {
            System.out.println("You do not have any " + currency.getName() + " withdraw from.");
        }
    }
    public boolean containsCurrency(Currency currency) {
        for (Map.Entry<Currency, Double> portfolioEntry : this.currencyShares.entrySet()) {
            if (isSameDenominationStatic(currency, portfolioEntry.getKey())) {
                return true;
            }
        }
        System.out.println("Your portfolio does not contain " + currency.getName() + ".");
        return false;
    }
    public double getSharesFromCurrency(Currency currency) {
        if (this.containsCurrency(currency)) {
            return this.currencyShares.get(currency);
        } else {
            System.out.println("Your portfolio does not contain " + currency.getName() + ". Returning 0.");
            return 0;
        }
    }
    public boolean hasEnoughShares(Currency currency, double amount) {
        if (this.containsCurrency(currency)) {
            if (this.currencyShares.get(currency) - amount >= 0) {
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("You do not have any shares of " + currency.getName() + ".");
            return false;
        }
    }

    //==========================-Utility-Methods-=============================
    public double dollarsToShares(Currency currency, double dollars) {
        return dollars * currency.getValue();
    }
    public double sharesToDollars(Currency currency, double shares) {
        return shares * currency.getValue();
    }

}
