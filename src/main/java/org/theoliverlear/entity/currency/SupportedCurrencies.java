package org.theoliverlear.entity.currency;
//=================================-Imports-==================================
import java.util.ArrayList;

public class SupportedCurrencies {
    //============================-Constants-=================================
    public static final Currency BITCOIN = new Currency("Bitcoin", "BTC","https://api.coinbase.com/v2/prices/BTC-USD/spot");
    public static final Currency ETHEREUM = new Currency("Ethereum", "ETH","https://api.coinbase.com/v2/prices/ETH-USD/spot");
    public static final Currency LITECOIN = new Currency("Litecoin", "LTC","https://api.coinbase.com/v2/prices/LTC-USD/spot");
    public static final Currency DOGECOIN = new Currency("Dogecoin", "DOGE","https://api.coinbase.com/v2/prices/DOGE-USD/spot");
    public static final Currency CARDANO = new Currency("Cardano", "ADA","https://api.coinbase.com/v2/prices/ADA-USD/spot");
    public static final Currency RIPPLE = new Currency("Ripple", "XRP","https://api.coinbase.com/v2/prices/XRP-USD/spot");
    public static final Currency POLKADOT = new Currency("Polkadot", "DOT","https://api.coinbase.com/v2/prices/DOT-USD/spot");
    public static final Currency BITCOIN_CASH = new Currency("Bitcoin Cash", "BCH","https://api.coinbase.com/v2/prices/BCH-USD/spot");
    public static final Currency CHAINLINK = new Currency("Chainlink", "LINK","https://api.coinbase.com/v2/prices/LINK-USD/spot");
    public static final Currency STELLAR = new Currency("Stellar", "XLM","https://api.coinbase.com/v2/prices/XLM-USD/spot");
    public static final Currency UNISWAP = new Currency("Uniswap", "UNI","https://api.coinbase.com/v2/prices/UNI-USD/spot");
    public static final Currency LITECOIN_CASH = new Currency("Litecoin Cash", "LCC","https://api.coinbase.com/v2/prices/LCC-USD/spot");
    public static final Currency BINANCE_COIN = new Currency("Binance Coin", "BNB","https://api.coinbase.com/v2/prices/BNB-USD/spot");
    public static final Currency TETHER = new Currency("Tether", "USDT","https://api.coinbase.com/v2/prices/USDT-USD/spot");
    public static final Currency EOS = new Currency("EOS", "EOS","https://api.coinbase.com/v2/prices/EOS-USD/spot");
    public static final Currency MONERO = new Currency("Monero", "XMR","https://api.coinbase.com/v2/prices/XMR-USD/spot");
    public static final Currency TRON = new Currency("Tron", "TRX","https://api.coinbase.com/v2/prices/TRX-USD/spot");
    public static final Currency TEZOS = new Currency("Tezos", "XTZ","https://api.coinbase.com/v2/prices/XTZ-USD/spot");
    public static final Currency STELLAR_LUMENS = new Currency("Stellar Lumens", "XLM","https://api.coinbase.com/v2/prices/XLM-USD/spot");
    public static final Currency SHIBA_INU = new Currency("Shiba Inu", "SHIB","https://api.coinbase.com/v2/prices/SHIB-USD/spot");
    public static final ArrayList<Currency> SUPPORTED_CURRENCIES = new ArrayList<>();
    //==========================-Static-Actions-==============================
    static {
        SUPPORTED_CURRENCIES.add(BITCOIN);
        SUPPORTED_CURRENCIES.add(ETHEREUM);
        SUPPORTED_CURRENCIES.add(LITECOIN);
        SUPPORTED_CURRENCIES.add(DOGECOIN);
        SUPPORTED_CURRENCIES.add(CARDANO);
        SUPPORTED_CURRENCIES.add(RIPPLE);
        SUPPORTED_CURRENCIES.add(POLKADOT);
        SUPPORTED_CURRENCIES.add(BITCOIN_CASH);
        SUPPORTED_CURRENCIES.add(CHAINLINK);
        SUPPORTED_CURRENCIES.add(STELLAR);
        SUPPORTED_CURRENCIES.add(UNISWAP);
        SUPPORTED_CURRENCIES.add(LITECOIN_CASH);
        SUPPORTED_CURRENCIES.add(BINANCE_COIN);
        SUPPORTED_CURRENCIES.add(TETHER);
        SUPPORTED_CURRENCIES.add(EOS);
        SUPPORTED_CURRENCIES.add(MONERO);
        SUPPORTED_CURRENCIES.add(TRON);
        SUPPORTED_CURRENCIES.add(TEZOS);
        SUPPORTED_CURRENCIES.add(STELLAR_LUMENS);
        SUPPORTED_CURRENCIES.add(SHIBA_INU);
    }
}
