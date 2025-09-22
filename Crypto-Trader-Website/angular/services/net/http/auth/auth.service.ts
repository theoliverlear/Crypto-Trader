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
    private readonly base = `${environment.apiUrl}/auth`;

    constructor(private http: HttpClient,
                private signupService: SignupService,
                private loginService: LoginService,
                private tokenRefreshService: TokenRefreshService,
                private logoutService: LogoutService) {}

    /**
     * Sign up using email/password.
     * Mirrors login but calls /auth/signup.
     */
    // signup(request: { email: string; password: string; username?: string }): Observable<AuthResponse> {
    //     const url = `${this.base}/signup`;
    //     return new Observable<AuthResponse>((subscriber) => {
    //         (async () => {
    //             await this.keys.ensureKeys();
    //             const dpop = await this.proofs.buildProof('POST', url);
    //             const headers = new HttpHeaders({ 'DPoP': dpop });
    //             this.http.post<AuthResponse>(url, request, { headers, withCredentials: true }).pipe(
    //                 tap(res => { if (res?.authorized && res?.token) this.tokenStore.setToken(res.token as any); })
    //             ).subscribe({
    //                 next: (res) => { subscriber.next(res); subscriber.complete(); },
    //                 error: (err) => subscriber.error(err)
    //             });
    //         })();
    //     });
    // }
    
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
    // login(req: LoginRequest): Observable<AuthResponse> {
    //     const url = `${this.base}/login`;
    //     return new Observable<AuthResponse>((subscriber) => {
    //         (async () => {
    //             await this.keys.ensureKeys();
    //             const dpop = await this.proofs.buildProof('POST', url);
    //             const headers = new HttpHeaders({
    //                 'DPoP': dpop
    //             });
    //             this.http.post<AuthResponse>(url, req, { headers, withCredentials: true }).pipe(
    //                 tap(res => { if (res?.authorized && res?.token) this.tokenStore.setToken(res.token); })
    //             ).subscribe({
    //                 next: (res) => { subscriber.next(res); subscriber.complete(); },
    //                 error: (err) => subscriber.error(err)
    //             });
    //         })();
    //     });
    // }

    login(request: LoginRequest): Observable<AuthResponse> {
        return this.loginService.login(request);
    }

    /**
     * Refresh the access token using the HttpOnly refresh cookie, bound to the same DPoP key.
     * Sends a DPoP proof and sets withCredentials so the cookie is included. Updates the in-memory token.
     *
     * @returns an observable that emits the fresh access token string
     */
    // refresh(): Observable<string> {
    //     const url = `${this.base}/refresh`;
    //     return new Observable<string>((subscriber) => {
    //         (async () => {
    //             await this.keys.ensureKeys();
    //             const current = this.tokenStore.getToken() || undefined;
    //             const dpop = await this.proofs.buildProof('POST', url, current);
    //             const headers = new HttpHeaders({ 'DPoP': dpop });
    //             this.http.post<AuthResponse>(url, {}, { headers, withCredentials: true }).subscribe({
    //                 next: (res) => {
    //                     if (res?.authorized && res?.token) {
    //                         this.tokenStore.setToken(res.token);
    //                         subscriber.next(res.token);
    //                         subscriber.complete();
    //                     } else {
    //                         this.tokenStore.clear();
    //                         subscriber.error(new Error('Unauthorized'));
    //                     }
    //                 },
    //                 error: (err) => {
    //                     this.tokenStore.clear();
    //                     subscriber.error(err);
    //                 }
    //             });
    //         })();
    //     });
    // }
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
    // logout(): Observable<void> {
    //     const url = `${this.base}/logout`;
    //     return new Observable<void>((subscriber) => {
    //         (async () => {
    //             await this.keys.ensureKeys();
    //             // Capture current token for Authorization header, but clear storage BEFORE request to avoid race with guards
    //             const current = this.tokenStore.getToken() || undefined;
    //             const dpop = await this.proofs.buildProof('POST', url, current);
    //             let headers = new HttpHeaders({ 'DPoP': dpop });
    //             if (current) {
    //                 headers = headers.set('Authorization', `DPoP ${current}`);
    //             }
    //             // Clear token now so subsequent requests (e.g., guards) won't attach it during the in-flight logout
    //             this.tokenStore.clear();
    //             this.http.post(url, {}, { withCredentials: true, headers }).subscribe({
    //                 next: () => { this.keys.clear(); subscriber.next(); subscriber.complete(); },
    //                 error: (err) => { this.keys.clear(); subscriber.error(err); }
    //             });
    //         })();
    //     });
    // }
    logout(): Observable<AuthResponse> {
        return this.logoutService.logout();
    }
}
