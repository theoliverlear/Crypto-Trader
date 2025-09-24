import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, map, tap} from "rxjs";
import {environment} from "../../../../environments/environment";
import {
    AuthResponse,
    LoginRequest,
    SignupRequest
} from "../../../../models/auth/types";
import {SignupService} from "./access/signup.service";
import {LoginService} from "./access/login.service";
import {TokenRefreshService} from "./token/token-refresh.service";
import {LogoutService} from "./access/logout.service";

/**
 * Angular AuthService implementing DPoP-bound authentication.
 *
 * Responsibilities:
 * - login(): generates/uses a DPoP key, sends DPoP proof + JKT, stores the access token in memory.
 * - refresh(): sends DPoP proof with withCredentials to rotate refresh cookie and obtain a new access token.
 * - logout(): clears in-memory token and key material, calls server to revoke refresh tokens.
 */
@Injectable({ 
    providedIn: 'root' 
})
export class AuthService {

    constructor(private signupService: SignupService,
                private loginService: LoginService,
                private tokenRefreshService: TokenRefreshService,
                private logoutService: LogoutService) {}

    /**
     * Sign up using email/password.
     * Mirrors login but calls /auth/signup.
     */
    signup(request: SignupRequest): Observable<AuthResponse> {
        return this.signupService.signup(request);
    }

    /**
     * Log in using credentials.
     *
     * What this does:
     * - Generates (or reuses) the browser’s DPoP key pair.
     * - Builds a one-time DPoP proof for POST /auth/login.
     * - Stores the returned short-lived access token in memory only.
     *
     * @param request the email/password payload
     * @returns an observable of the AuthResponse (authorized + token when successful)
     */
    login(request: LoginRequest): Observable<AuthResponse> {
        return this.loginService.login(request);
    }

    /**
     * Refresh the access token using the HttpOnly refresh cookie, bound to the same DPoP key.
     * Sends a DPoP proof and sets withCredentials so the cookie is included. Updates the in-memory token.
     *
     * @returns an observable that emits the fresh access token string
     */
    refresh(): Observable<string> {
        return this.tokenRefreshService.refreshToken();
    }

    /**
     * Log out of the current session.
     * - Calls server to revoke the refresh token family and clear the cookie (withCredentials).
     * - Clears the in-memory access token and deletes local DPoP key material.
     *
     * @returns an observable that completes when logout has finished
     */
    logout(): Observable<AuthResponse> {
        return this.logoutService.logout();
    }
}
