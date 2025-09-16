import {Component, EventEmitter, Input, Output} from "@angular/core";
import {
    AuthPopup,
    AuthType,
    WebSocketCapable
} from "@theoliverlear/angular-suite";
import {
    SignupWebSocketService
} from "../../../../services/net/websocket/signup-websocket.service";
import {
    LoginWebSocketService
} from "../../../../services/net/websocket/login-websocket.service";
import {Subscription} from "rxjs";
import {SignupCredentials} from "../../../../models/auth/SignupCredentials";
import {SignupRequest} from "../../../../models/auth/types";

@Component({
    selector: 'auth-console',
    standalone: false,
    templateUrl: './auth-console.component.html',
    styleUrls: ['./auth-console.component.scss']
})
export class AuthConsoleComponent implements WebSocketCapable {
    @Input() currentAuthType: AuthType = AuthType.SIGN_UP;
    @Output() authPopupEvent: EventEmitter<AuthPopup> = new EventEmitter<AuthPopup>();
    webSocketSubscriptions: Record<string, Subscription> = {};
    constructor(private signupWebSocket: SignupWebSocketService,
                private loginWebSocket: LoginWebSocketService) {

    }
    
    attemptSignup(signupCredentials: SignupCredentials) {
        if (signupCredentials.getSubmitIssue() !== AuthPopup.NONE) {
            this.signup(signupCredentials.getSignupRequest());
        } else {
            this.emitAuthPopup(signupCredentials.getSubmitIssue());
        }
    }
    
    signup(signupRequest: SignupRequest) {
        this.signupWebSocket.sendMessage(signupRequest);
    }

    emitAuthPopup(authPopup: AuthPopup): void {
        this.authPopupEvent.emit(authPopup);
    }

    setAuthType(authType: AuthType): void {
        this.currentAuthType = authType;
    }
    isSignupSection(): boolean {
        return this.currentAuthType === AuthType.SIGN_UP;
    }
    isLoginSection(): boolean {
        return this.currentAuthType === AuthType.LOGIN;
    }

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