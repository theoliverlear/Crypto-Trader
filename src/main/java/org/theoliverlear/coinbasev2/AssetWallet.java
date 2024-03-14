package org.theoliverlear.coinbasev2;

public class AssetWallet {
    String id;
    String currencyName;
    String currencyCode;
    String assetId;
    String balance;
    public AssetWallet(String id, String currencyName, String currencyCode, String assetId, String balance) {
        this.id = id;
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.assetId = assetId;
        this.balance = balance;
    }
}
