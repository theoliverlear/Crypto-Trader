package org.theoliverlear.comm.request;

import lombok.Data;

@Data
public class AssetValueRequest {
    private String currencyCode;
    private double shares;
    public AssetValueRequest(String currencyCode, double shares) {
        this.currencyCode = currencyCode;
        this.shares = shares;
    }
}
