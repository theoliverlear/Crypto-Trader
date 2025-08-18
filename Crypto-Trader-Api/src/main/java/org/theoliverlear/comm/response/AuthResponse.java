package org.theoliverlear.comm.response;

import lombok.Data;
import org.theoliverlear.model.http.AuthStatus;

@Data
public class AuthResponse {
    private boolean authorized;
    public AuthResponse(AuthStatus authStatus) {
        this.authorized = authStatus.isAuthorized;
    }
}
