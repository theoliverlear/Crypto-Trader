import {Injectable} from "@angular/core";
import {WebSocketService} from "@theoliverlear/angular-suite";
import {AuthResponse, LoginRequest} from "../../../models/auth/types";
import {environment} from "../../../environments/environment";
import {TokenStorageService} from "../../auth/token-storage.service";

@Injectable({
    providedIn: 'root'
})
export class LoginWebSocketService extends WebSocketService<LoginRequest, AuthResponse> {
    private static readonly URL: string = `${environment.websocketUrl}/login`;
    constructor(private tokens: TokenStorageService) {
        // Prefer Authorization header for WS if underlying client supports headers; fallback to query param
        // Our server accepts both Authorization: Bearer <token> and ?token=<token>
        const token = tokens.getToken();
        const url = token ? `${LoginWebSocketService.URL}?token=${encodeURIComponent(token)}` : LoginWebSocketService.URL;
        super(url);
    }
}