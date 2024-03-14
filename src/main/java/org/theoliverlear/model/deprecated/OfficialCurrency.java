package org.theoliverlear.model.deprecated;

public enum OfficialCurrency {
    BTC("Bitcoin", "BTC", "https://api.coinbase.com/v2/prices/BTC-USD/spot"),
    SHIB("Shiba Inu", "SHIB", "https://api.coinbase.com/v2/prices/SHIB-USD/spot"),
    ETH("Ethereum", "ETH", "https://api.coinbase.com/v2/prices/ETH-USD/spot"),
    DOGE("Dogecoin", "DOGE", "https://api.coinbase.com/v2/prices/DOGE-USD/spot"),
    LTC("Litecoin", "LTC", "https://api.coinbase.com/v2/prices/LTC-USD/spot"),
    ADA("Cardano", "ADA", "https://api.coinbase.com/v2/prices/ADA-USD/spot"),
    SOL("Solana", "SOL", "https://api.coinbase.com/v2/prices/SOL-USD/spot"),
    MATIC("Polygon", "MATIC", "https://api.coinbase.com/v2/prices/MATIC-USD/spot"),
    LINK("Chainlink", "LINK", "https://api.coinbase.com/v2/prices/LINK-USD/spot"),
    XLM("Stellar Lumens", "XLM", "https://api.coinbase.com/v2/prices/XLM-USD/spot"),
    DOT("Polkadot", "DOT", "https://api.coinbase.com/v2/prices/DOT-USD/spot");
    public final String name, currencyCode, urlPath;
    OfficialCurrency(String name, String currencyCode, String urlPath) {
        this.name = name;
        this.currencyCode = currencyCode;
        this.urlPath = urlPath;
    }
    public String getName() {
        return this.name;
    }
    public String getCurrencyCode() {
        return this.currencyCode;
    }
    public String getUrlPath() {
        return this.urlPath;
    }
}
