import {Injectable} from "@angular/core";
import {HttpClientService} from "@theoliverlear/angular-suite";
import {environment} from "../../../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable, map, BehaviorSubject} from "rxjs";
import {AuthResponse} from "../../../../../models/auth/types";
import {TokenStorageService} from "../../../../auth/token-storage.service";

@Injectable({
    providedIn: 'root'
})
/**
 * Lightweight auth status client for the UI.
 *
 * - Calls GET /auth/logged-in to check whether the current request is authenticated.
 * - Does not send Authorization/DPoP headers; the endpoint is public and simply reflects server state.
 * - If the server returns authorized=true but no token (which is normal), we enrich the response with the
 *   current in-memory token so components that need it don’t have to query storage separately.
 */
export class LoggedInService extends HttpClientService<any, AuthResponse> {
    private static readonly URL: string = `${environment.apiUrl}/auth/logged-in`;
    private readonly authState$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
    
    constructor(httpClient: HttpClient, private tokenStorage: TokenStorageService) {
        super(LoggedInService.URL, httpClient);
    }
    
    /**
     * Query the server for current auth status.
     * Returns an AuthResponse; if authorized=true and no token is present, we attach the in-memory token for convenience.
     */
    public isLoggedIn(): Observable<AuthResponse> {
        return this.get().pipe(
            map((response: AuthResponse) => {
                if (response?.authorized && !response.token) {
                    this.authState$.next(response?.authorized || false);
                    const token: string = this.tokenStorage.getToken();
                    return token ? { ...response, token } : response;
                }
                return response;
            })
        );
    }
    
    public getAuthState(): Observable<boolean> {
        return this.authState$.asObservable();
    }
}