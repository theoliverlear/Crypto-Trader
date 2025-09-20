import {
    Component,
    EventEmitter,
    Input,
    OnDestroy,
    Output
} from "@angular/core";
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
import {
    AuthResponse,
    LoginRequest,
    SignupRequest
} from "../../../../models/auth/types";
import {Router} from "@angular/router";
import {LoginCredentials} from "../../../../models/auth/LoginCredentials";
import {
    TokenStorageService
} from "../../../../services/auth/token-storage.service";

@Component({
    selector: 'auth-console',
    standalone: false,
    templateUrl: './auth-console.component.html',
    styleUrls: ['./auth-console.component.scss']
})
export class AuthConsoleComponent implements WebSocketCapable, OnDestroy {
    @Input() currentAuthType: AuthType = AuthType.SIGN_UP;
    @Output() authPopupEvent: EventEmitter<AuthPopup> = new EventEmitter<AuthPopup>();
    attempts: number = 0;
    webSocketSubscriptions: Record<string, Subscription> = {};
    constructor(private signupWebSocket: SignupWebSocketService,
                private loginWebSocket: LoginWebSocketService,
                private router: Router,
                private tokenStorageService: TokenStorageService) {

    }
    
    attemptLogin(loginCredentials: LoginCredentials): void {
        if (loginCredentials.isFilledFields()) {
            this.login(loginCredentials.getRequest())
        } else {
            this.emitAuthPopup(AuthPopup.FILL_ALL_FIELDS);
        }
    }
    
    login(loginRequest: LoginRequest): void {
        this.loginWebSocket.sendMessage(loginRequest);
    }
    
    saveToken(authResponse: AuthResponse) {
        if (authResponse.token) {
            this.tokenStorageService.setToken(authResponse.token);
        }
    }
    
    attemptSignup(signupCredentials: SignupCredentials): void {
        if (signupCredentials.getAnyIssue() === AuthPopup.NONE) {
            this.signup(signupCredentials.getSignupRequest());
        } else {
            this.emitAuthPopup(signupCredentials.getAnyIssue());
        }
    }
    
    signup(signupRequest: SignupRequest) {
        this.signupWebSocket.sendMessage(signupRequest);
    }

    emitAuthPopup(authPopup: AuthPopup): void {
        this.authPopupEvent.emit(authPopup);
    }

    setAuthType(authType: AuthType): void {
        this.emitAuthPopup(AuthPopup.NONE);
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
        console.log('[WS] Connecting signup socket…');
        this.signupWebSocket.connect();
        this.webSocketSubscriptions['signup'] = this.signupWebSocket.getMessages().subscribe({
            next: (authResponse: AuthResponse) => {
                console.log('[WS][signup] message:', authResponse);
                if (!authResponse) {
                    return;
                }
                if (authResponse.authorized) {
                    console.log("[WS][signup] Authorized");
                    this.saveToken(authResponse);
                    this.router.navigate(['/portfolio']);
                } else {
                    console.log("[WS][signup] Not authorized");
                    if (this.attempts !== 0) {
                        this.emitAuthPopup(AuthPopup.USERNAME_OR_EMAIL_EXISTS);
                    }
                }
            },
            error: (error) => {
                console.log('[WS][signup] error:', error);
            },
            complete: () => {
                console.log('[WS][signup] complete');
            }
        });
    }

    private initLoginWebSocket() {
        console.log('[WS] Connecting login socket…');
        this.loginWebSocket.connect();
        this.webSocketSubscriptions['login'] = this.loginWebSocket.getMessages().subscribe({
            next: (authResponse: AuthResponse) => {
                console.log('[WS][login] message:', authResponse);
                if (!authResponse) {
                    return;
                }
                if (authResponse.authorized) {
                    console.log("[WS][login] Authorized");
                    this.saveToken(authResponse);
                    this.router.navigate(['/portfolio']);
                } else {
                    console.log("[WS][login] Not authorized");
                    if (this.attempts !== 0) {
                        this.emitAuthPopup(AuthPopup.USERNAME_OR_EMAIL_EXISTS);
                    }
                }
            },
            error: (error) => {
                console.log('[WS][login] error:', error);
            },
            complete: () => {
                console.log('[WS][login] complete');
            }
        });
    }

    ngOnDestroy(): void {
        Object.values(this.webSocketSubscriptions).forEach((sub) => {
            try { 
                sub?.unsubscribe(); 
            } catch {}
        });
        this.webSocketSubscriptions = {};
        try { 
            this.signupWebSocket.disconnect(); 
        } catch {}
        try { 
            this.loginWebSocket.disconnect(); 
        } catch {}
    }


    protected readonly AuthPopup = AuthPopup;
}