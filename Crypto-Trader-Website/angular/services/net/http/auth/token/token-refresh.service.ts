import {Injectable} from "@angular/core";
import {
    HttpClientService,
    HttpOptions,
    httpOptionsWithCredentials
} from "@theoliverlear/angular-suite";
import {AuthResponse} from "../../../../../models/auth/types";
import {environment} from "../../../../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {TokenStorageService} from "../../../../auth/token-storage.service";
import {DpopKeyService} from "../../../../auth/dpop/dpop-key.service";
import {DpopProofService} from "../../../../auth/dpop/dpop-proof.service";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class TokenRefreshService extends HttpClientService<any, AuthResponse> {
    private static readonly URL: string = `${environment.apiUrl}/auth/refresh`;
    constructor(httpClient: HttpClient,
                private tokenStore: TokenStorageService,
                private keys: DpopKeyService,
                private proofs: DpopProofService) {
        super(TokenRefreshService.URL, httpClient);
    }

    public refreshToken(): Observable<string> {
        return new Observable<string>((subscriber) => {
            (async () => {
                await this.keys.ensureKeys();
                const current = this.tokenStore.getToken() || undefined;
                const dpop: string = await this.proofs.buildProof('POST', TokenRefreshService.URL, current);
                const headers = new HttpHeaders({
                    'DPoP': dpop
                });
                const options: HttpOptions = {
                    ...httpOptionsWithCredentials,
                    headers
                };
                this.post({}, true, options).subscribe({
                    next: (response) => {
                        if (response?.authorized && response?.token) {
                            this.tokenStore.setToken(response.token);
                            subscriber.next(response.token);
                            subscriber.complete();
                        } else {
                            this.tokenStore.clear();
                            subscriber.error(new Error('Unauthorized'));
                        }
                    },
                    error: (error) => {
                        this.tokenStore.clear();
                        subscriber.error(error);
                    }
                })
            })();
        });
    }
}