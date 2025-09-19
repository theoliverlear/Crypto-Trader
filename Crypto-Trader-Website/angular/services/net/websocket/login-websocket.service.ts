import {Injectable} from "@angular/core";
import {WebSocketService} from "@theoliverlear/angular-suite";
import {AuthResponse, LoginRequest} from "../../../models/auth/types";
import {environment} from "../../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class LoginWebSocketService extends WebSocketService<LoginRequest, AuthResponse> {
    private static readonly URL: string = `${environment.websocketUrl}/login`;
    constructor() {
        super(LoginWebSocketService.URL);
    }
}