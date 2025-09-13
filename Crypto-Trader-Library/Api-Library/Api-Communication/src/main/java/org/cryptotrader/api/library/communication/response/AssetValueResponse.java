package org.cryptotrader.api.library.communication.response;

import lombok.Data;

@Data
public class AssetValueResponse {
    public double value;
    public AssetValueResponse(double value) {
        this.value = value;
    }
}
