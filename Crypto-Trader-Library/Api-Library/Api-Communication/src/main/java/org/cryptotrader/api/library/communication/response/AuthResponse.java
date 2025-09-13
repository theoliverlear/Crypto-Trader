package org.cryptotrader.api.library.communication.response;

import lombok.Data;

@Data
public class AuthResponse {
    private boolean authorized;
    public AuthResponse(boolean isAuthorized) {
        this.authorized = isAuthorized;
    }
}
