package org.cryptotrader.api.library.communication.response;

import lombok.Data;

@Data
public class AuthResponse {
    private boolean authorized;
    private String token;
    public AuthResponse(boolean isAuthorized) {
        this.authorized = isAuthorized;
    }
    public AuthResponse(boolean isAuthorized, String token) {
        this.authorized = isAuthorized;
        this.token = token;
    }
}
