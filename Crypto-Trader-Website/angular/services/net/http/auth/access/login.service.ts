import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {
    HttpClientService, HttpOptions,
    httpOptionsWithCredentials
} from "@theoliverlear/angular-suite";
import {Observable, tap} from "rxjs";
import {environment} from "../../../../../environments/environment";
import {AuthResponse, LoginRequest, PersistMethod} from "../../../../../models/auth/types";
import {TokenStorageService} from "../../../../auth/token-storage.service";
import {DpopKeyService} from "../../../../auth/dpop/dpop-key.service";
import {DpopProofService} from "../../../../auth/dpop/dpop-proof.service";

@Injectable({
    providedIn: 'root'
})
export class LoginService extends HttpClientService<LoginRequest, AuthResponse> {
    private static readonly URL: string = `${environment.apiUrl}/auth/login`;

    constructor(private tokenStore: TokenStorageService,
                private keys: DpopKeyService,
                private proofs: DpopProofService) {
        super(LoginService.URL);
    }

    // public login(request: LoginRequest, _persist: PersistMethod = 'session'): Observable<AuthResponse> {
    //     // Delegate to AuthService to include DPoP headers and in-memory token storage
    //     return this.auth.login(request);
    // }

    public login(request: LoginRequest) {
        return new Observable<AuthResponse>((subscriber) => {
            (async () => {
                await this.keys.ensureKeys();
                const dpop: string = await this.proofs.buildProof('POST', LoginService.URL);
                const headers: HttpHeaders = new HttpHeaders({
                    'DPoP': dpop
                });
                const options: HttpOptions = {
                    ...httpOptionsWithCredentials,
                    headers
                };
                this.post(request, true, options).pipe(
                    tap(response => {
                        if (response?.authorized && response?.token) {
                            this.tokenStore.setToken(response.token as any);
                        }
                    })
                ).subscribe({
                    next: (response) => {
                        subscriber.next(response);
                        subscriber.complete();
                    },
                    error: (error) => {
                        subscriber.error(error);
                    }
                });
            })();
        });
    }
}
