import {Injectable} from "@angular/core";
import {WebSocketService} from "@theoliverlear/angular-suite";
import {AuthResponse, LoginRequest} from "../../../models/auth/types";
import {environment} from "../../../environments/environment";

/**
 * WebSocket client for the legacy login channel.
 *
 * Notes:
 * - The application now uses HTTP + DPoP for login. This WS client remains for backward
 *   compatibility and should not be used for authentication in production.
 * - If used, establish the socket only after a successful HTTP auth and do not send
 *   raw credentials over the wire.
 */
@Injectable({
    providedIn: 'root'
})
export class LoginWebSocketService extends WebSocketService<LoginRequest, AuthResponse> {
    private static readonly URL: string = `${environment.websocketUrl}/login`;
    constructor() {
        super(LoginWebSocketService.URL);
    }
}