package org.theoliverlear.coinbasev2;

public class CoinbaseAccount {
    String id;
    String name;
    String email;

    public CoinbaseAccount(String id) {
        this.id = id;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
