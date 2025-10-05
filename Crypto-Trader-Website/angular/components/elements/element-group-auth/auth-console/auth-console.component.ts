import {
    Component,
    EventEmitter,
    Input,
    OnDestroy, OnInit,
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
import {LoginService} from "../../../../services/net/http/auth/access/login.service";
import {SignupService} from "../../../../services/net/http/auth/access/signup.service";

@Component({
    selector: 'auth-console',
    standalone: false,
    templateUrl: './auth-console.component.html',
    styleUrls: ['./auth-console.component.scss']
})
/**
 * Authentication console component for the landing/authorize page.
 *
 * Responsibilities:
 * - Coordinates login and signup flows using the AuthService (HTTP + DPoP).
 * - Shows UI feedback via AuthPopup events.
 * - Avoids sending credentials over WebSockets; only the signup WS is used for
 *   ancillary messages, while login/signup are performed over HTTP.
 */
export class AuthConsoleComponent implements WebSocketCapable, OnDestroy, OnInit {
    @Input() currentAuthType: AuthType = AuthType.SIGN_UP;
    @Output() authPopupEvent: EventEmitter<AuthPopup> = new EventEmitter<AuthPopup>();
    attempts: number = 0;
    webSocketSubscriptions: Record<string, Subscription> = {};
    constructor(private signupWebSocket: SignupWebSocketService,
                private signupService: SignupService,
                private loginService: LoginService,
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
        this.loginService.login(loginRequest).subscribe({
            next: (authResponse: AuthResponse) => {
                if (authResponse?.authorized) {
                    this.saveToken(authResponse);
                    this.router.navigate(['/portfolio']);
                } else {
                    this.emitAuthPopup(AuthPopup.USERNAME_OR_EMAIL_EXISTS);
                }
            },
            error: (err) => {
                console.error('[HTTP][login] error:', err);
                this.emitAuthPopup(AuthPopup.USERNAME_OR_EMAIL_EXISTS);
            }
        });
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
        console.log("Sending signup request via HTTP /auth/signup");
        this.signupService.signup(signupRequest).subscribe({
            next: (authResponse: AuthResponse) => {
                if (authResponse?.authorized) {
                    this.saveToken(authResponse);
                    this.router.navigate(['/portfolio']);
                } else {
                    this.emitAuthPopup(AuthPopup.USERNAME_OR_EMAIL_EXISTS);
                }
            },
            error: (err) => {
                console.error('[HTTP][signup] error:', err);
                this.emitAuthPopup(AuthPopup.USERNAME_OR_EMAIL_EXISTS);
            }
        });
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
        // No WebSocket auth; using HTTP + DPoP
    }
    initializeWebSockets(): void {
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
    }


    protected readonly AuthPopup = AuthPopup;
}