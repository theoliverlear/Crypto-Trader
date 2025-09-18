import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {HttpClientService} from "@theoliverlear/angular-suite";
import {Observable, tap} from "rxjs";
import {environment} from "../../../environments/environment";
import {
    AuthResponse,
    LoginRequest,
    PersistMethod
} from "../../../models/auth/types";
import {TokenStorageService} from "../../auth/token-storage.service";

@Injectable({ providedIn: 'root' })
export class LoginService extends HttpClientService<LoginRequest, AuthResponse> {
    private static readonly URL: string = `${environment.apiUrl}/auth/login`;

    constructor(httpClient: HttpClient, private tokenStorage: TokenStorageService) {
        super(LoginService.URL, httpClient);
    }

    public login(request: LoginRequest, persist: PersistMethod = 'session'): Observable<AuthResponse> {
        return this.post(request).pipe(
            tap((response: AuthResponse) => {
                if (this.isValidAuthResponse(response)) {
                    this.tokenStorage.setToken(response.token, persist);
                }
            })
        );
    }

    private isValidAuthResponse(response: AuthResponse) {
        return response && response.authorized && response.token;
    }
}
