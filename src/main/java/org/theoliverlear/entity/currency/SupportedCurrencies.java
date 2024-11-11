package org.theoliverlear.entity.currency;
//=================================-Imports-==================================
import java.util.ArrayList;
import java.util.List;

public class SupportedCurrencies {
    //============================-Constants-=================================
    public static final Currency BITCOIN = new Currency("Bitcoin", "BTC");
    public static final Currency ETHEREUM = new Currency("Ethereum", "ETH");
    public static final Currency LITECOIN = new Currency("Litecoin", "LTC");
    public static final Currency DOGECOIN = new Currency("Dogecoin", "DOGE");
    public static final Currency CARDANO = new Currency("Cardano", "ADA");
    public static final Currency RIPPLE = new Currency("Ripple", "XRP");
    public static final Currency POLKADOT = new Currency("Polkadot", "DOT");
    public static final Currency BITCOIN_CASH = new Currency("Bitcoin Cash", "BCH");
    public static final Currency CHAINLINK = new Currency("Chainlink", "LINK");
    public static final Currency STELLAR = new Currency("Stellar", "XLM");
    public static final Currency UNISWAP = new Currency("Uniswap", "UNI");
    public static final Currency LITECOIN_CASH = new Currency("Litecoin Cash", "LCC");
    public static final Currency BINANCE_COIN = new Currency("Binance Coin", "BNB");
    public static final Currency TETHER = new Currency("Tether", "USDT");
    public static final Currency EOS = new Currency("EOS", "EOS");
    public static final Currency MONERO = new Currency("Monero", "XMR");
    public static final Currency TRON = new Currency("Tron", "TRX");
    public static final Currency TEZOS = new Currency("Tezos", "XTZ");
    public static final Currency STELLAR_LUMENS = new Currency("Stellar Lumens", "XLM");
    public static final Currency SHIBA_INU = new Currency("Shiba Inu", "SHIB");
    public static final List<Currency> SUPPORTED_CURRENCIES = new ArrayList<>();
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
