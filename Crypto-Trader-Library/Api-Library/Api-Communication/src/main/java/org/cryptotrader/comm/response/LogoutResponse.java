package org.cryptotrader.comm.response;

import lombok.Data;

@Data
public class LogoutResponse {
    private boolean isLoggedOut;
    public LogoutResponse(boolean isLoggedOut) {
        this.isLoggedOut = isLoggedOut;
    }
}