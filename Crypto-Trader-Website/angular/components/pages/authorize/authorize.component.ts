import {Component, OnInit} from "@angular/core";
import {AuthPopup, WebSocketCapable} from "@theoliverlear/angular-suite";
import {Subscription} from "rxjs";
import {
    SignupWebSocketService
} from "../../../services/net/websocket/signup-websocket.service";
import {
    LoginWebSocketService
} from "../../../services/net/websocket/login-websocket.service";
import {LoginCredentials} from "../../../models/auth/LoginCredentials";
import {SignupCredentials} from "../../../models/auth/SignupCredentials";

@Component({
    selector: 'authorize',
    standalone: false,
    templateUrl: './authorize.component.html',
    styleUrls: ['./authorize.component.scss']
})
export class AuthorizeComponent implements WebSocketCapable, OnInit {
    private loginCredentials: LoginCredentials;
    private signupCredentials: SignupCredentials;
    private authPopup: AuthPopup = AuthPopup.NONE;
    constructor(private signupWebSocket: SignupWebSocketService,
                private loginWebSocket: LoginWebSocketService) {

    }
    
    setAuthPopup(authPopup: AuthPopup): void {
        this.authPopup = authPopup;
    }
    
    webSocketSubscriptions: Record<string, Subscription> = {};

    ngOnInit(): void {
        this.initializeWebSockets();
    }
    initializeWebSockets(): void {
        this.initSignupWebSocket();
        this.initLoginWebSocket();
    }

    private initSignupWebSocket() {
        this.signupWebSocket.connect();
        this.webSocketSubscriptions['signup'] = this.signupWebSocket.getMessages().subscribe({
            next: (message) => {
                console.log(message);
            },
            error: (error) => {
                console.log(error);
            },
            complete: () => {
                console.log('complete');
            }
        });
    }
    
    private initLoginWebSocket() {
        this.loginWebSocket.connect();
        this.webSocketSubscriptions['login'] = this.loginWebSocket.getMessages().subscribe({
            next: (message) => {
                console.log(message);
            },
            error: (error) => {
                console.log(error);
            },
            complete: () => {
                console.log('complete');
            }
        });
    }

    protected readonly AuthPopup = AuthPopup;
}