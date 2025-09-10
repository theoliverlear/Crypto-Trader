package org.cryptotrader.api.library.comm.response;

import lombok.Data;

@Data
public class LogoutResponse {
    private boolean isLoggedOut;
    public LogoutResponse(boolean isLoggedOut) {
        this.isLoggedOut = isLoggedOut;
    }
}