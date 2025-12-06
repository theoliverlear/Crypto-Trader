import {Injectable} from "@angular/core";
import {
    HttpClientService,
    HttpOptions,
    httpOptionsWithCredentials
} from "@theoliverlear/angular-suite";
import {environment} from "../../../../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, map} from "rxjs";
import {AuthResponse} from "../../../../../models/auth/types";
import {TokenStorageService} from "../../../../auth/token-storage.service";
import {DpopKeyService} from "../../../../auth/dpop/dpop-key.service";
import {DpopProofService} from "../../../../auth/dpop/dpop-proof.service";
import {LoggedInService} from "../status/logged-in.service";

@Injectable({
    providedIn: 'root'
})
export class LogoutService extends HttpClientService<any, AuthResponse> {
    private static readonly URL = `${environment.apiUrl}/auth/logout`;

    constructor(private tokenStore: TokenStorageService,
                private keys: DpopKeyService,
                private proofs: DpopProofService,
                private loggedInService: LoggedInService) {
        super(LogoutService.URL);
    }

    public logout(): Observable<AuthResponse> {
        return new Observable<AuthResponse>((subscriber) => {
            (async () => {
                await this.keys.ensureKeys();
                const current: string = this.tokenStore.getToken() || undefined;
                const dpop: string = await this.proofs.buildProof('POST', LogoutService.URL);
                let headers: HttpHeaders = new HttpHeaders({
                    'DPoP': dpop
                });
                if (current) {
                    headers = headers.set('Authorization', `DPoP ${current}`);
                }
                this.tokenStore.clear();
                const options: HttpOptions = {
                    ...httpOptionsWithCredentials,
                    headers
                }
                this.post({}, true, options).subscribe({
                    next: (response) => {
                        this.keys.clear();
                        subscriber.next(response);
                        subscriber.complete();
                        this.loggedInService.isLoggedIn();
                    },
                    error: (error) => {
                        this.keys.clear();
                        if (error.status !== 401) {
                            subscriber.error(error);
                        }
                    }
                });
            })();
        });
    }
}