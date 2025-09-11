import {Injectable} from "@angular/core";
import {WebSocketService} from "@theoliverlear/angular-suite";
import {AuthResponse, SignupRequest} from "../../../models/auth/types";
import {environment} from "../../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class SignupWebSocketService extends WebSocketService<SignupRequest, AuthResponse> {
    private static readonly URL: string = `${environment.websocketUrl}/signup`;
    constructor() {
        super(SignupWebSocketService.URL);
    }
}