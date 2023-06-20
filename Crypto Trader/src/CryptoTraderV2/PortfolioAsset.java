package CryptoTraderV2;

import java.io.IOException;

public class PortfolioAsset {
    Currency currency;
    double targetPrice;
    double shares;
    double portfolioDollars;
    CryptoTraderDatabase cryptoTraderDatabase;
    double totalValue;

    public PortfolioAsset(Currency currency, double targetPrice, double shares, double portfolioDollars) throws IOException {
        this.currency = currency;
        this.targetPrice = targetPrice;
        this.shares = shares;
        this.portfolioDollars = portfolioDollars;
        this.cryptoTraderDatabase = new CryptoTraderDatabase(this.currency);
        this.totalValue = (this.shares * this.currency.getValue()) + this.portfolioDollars;
    }

    public double getTotalValue() {
        return (this.shares * this.currency.getValue()) + this.portfolioDollars;
    }

    public CryptoTraderDatabase getCryptoTraderDatabase() {
        return this.cryptoTraderDatabase;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public double getTargetPrice() {
        return this.targetPrice;
    }

    public double getShares() {
        return this.shares;
    }

    public double getPortfolioDollars() {
        return this.portfolioDollars;
    }

    public void sellAsset() {
        double currentValue = this.currency.getValue();
        this.portfolioDollars += this.shares * currentValue;
        System.out.println(this.currency.getName() + ": Sold " + this.shares + " shares for " +
                           this.portfolioDollars + " dollars.");
        this.shares = 0;
        this.targetPrice = currentValue;
        this.cryptoTraderDatabase.commandQuery("INSERT INTO PortfolioHistory " +
                "(time_event, currency_code, currency_name, exchange_rate," +
                " exchange_rate_formatted, target_price, exchange_type, shares, dollars, total_value) " +
                "VALUES (SWITCHOFFSET(GETDATE(), '-05:00'), '" + this.currency.getCurrencyCode() + "'," +
                " '" + this.currency.getName() + "', " + this.currency.getValue() + ", '" +
                this.currency.getFormattedValue() + "', "  + this.targetPrice + ", 'sell', " + this.shares + ", " +
                this.portfolioDollars + ", " + this.getTotalValue() + ");");
        this.cryptoTraderDatabase.commandQuery("UPDATE Portfolio SET exchange_rate = " + this.currency.getValue() +
                ", exchange_rate_formatted = '" + this.currency.getFormattedValue() +
                "', target_price = " + this.targetPrice + ", shares = " + this.shares + ", dollars = " + this.portfolioDollars + "," +
                " total_value = " + this.getTotalValue() + " , time_updated = SWITCHOFFSET(GETDATE(), '-05:00')" +
                " WHERE currency_code = '" + this.currency.getCurrencyCode() + "';");

    }
    public void buyAsset() {
        double currentValue = this.currency.getValue();
        this.shares += this.portfolioDollars / currentValue;
        System.out.println(this.currency.getName() + ": Bought " + this.shares + " shares with " +
                           this.portfolioDollars + " dollars.");
        this.portfolioDollars = 0;
        this.targetPrice = currentValue;
        this.cryptoTraderDatabase.commandQuery("INSERT INTO PortfolioHistory (time_event, currency_code, currency_name," +
                " exchange_rate, exchange_rate_formatted, target_price, exchange_type, shares, dollars, total_value)" +
                " VALUES (SWITCHOFFSET(GETDATE(), '-05:00'), '" + this.currency.getCurrencyCode() + "', " +
                "'" + this.currency.getName() + "', " + this.currency.getValue() + ", '" +
                this.currency.getFormattedValue() + "'," + this.targetPrice + ", 'buy', " + this.shares + ", " +
                this.portfolioDollars + ", " + this.getTotalValue() + ");");
        this.cryptoTraderDatabase.commandQuery("UPDATE Portfolio SET exchange_rate = " + this.currency.getValue() +
                ", exchange_rate_formatted = '" + this.currency.getFormattedValue() +
                "', target_price = " + this.targetPrice + ", shares = " + this.shares + ", dollars = " + this.portfolioDollars +
                ", total_value = " + this.getTotalValue() + ",time_updated = SWITCHOFFSET(GETDATE(), '-05:00') " +
                "WHERE currency_code = '" + this.currency.getCurrencyCode() + "';");

    }
    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }
    public void poll() throws IOException {
        double currentValue = this.currency.getUpdatedValue();
        if (currentValue > this.targetPrice) {
            if (this.shares > 0) {
                this.sellAsset();
            }
        } else if (currentValue < this.targetPrice) {
            if (this.portfolioDollars > 0) {
                this.buyAsset();
            }
        }
    }
}
