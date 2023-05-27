package CryptoTraderV2;

public class PortfolioAsset {
    Currency currency;
    double targetPrice;
    double shares;
    double portfolioDollars;
    public PortfolioAsset(Currency currency, double buyPrice, double shares, double portfolioDollars) {
        this.currency = currency;
        this.targetPrice = buyPrice;
        this.shares = shares;
        this.portfolioDollars = portfolioDollars;
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
        System.out.println(this.currency.getName() + ": Sold " + this.shares + " shares for " + this.portfolioDollars + " dollars.");
        this.shares = 0;
        this.targetPrice = currentValue;
    }
    public void buyAsset() {
        double currentValue = this.currency.getValue();
        this.shares += this.portfolioDollars / currentValue;
        System.out.println(this.currency.getName() + ": Bought " + this.shares + " shares with " + this.portfolioDollars + " dollars.");
        this.portfolioDollars = 0;
        this.targetPrice = currentValue;
    }
    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }
    public void poll() {
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
// Create new portfolio asset, determine buy or sell, put current shares/dollars in database

// Create new table called portfolio in SQL database
// Create new table called portfolio_assets in SQL database
// Create new table called purchase_log in SQL database
// purchase/sold currency, time, price, shares, dollars
