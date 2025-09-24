import {
    HttpClientService,
    HttpOptions,
    httpOptionsWithCredentials
} from "@theoliverlear/angular-suite";
import {AuthResponse, SignupRequest} from "../../../../../models/auth/types";
import {environment} from "../../../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {TokenStorageService} from "../../../../auth/token-storage.service";
import {DpopKeyService} from "../../../../auth/dpop/dpop-key.service";
import {DpopProofService} from "../../../../auth/dpop/dpop-proof.service";
import {Observable, tap} from "rxjs";
import {Injectable} from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class SignupService extends HttpClientService<SignupRequest, AuthResponse> {
    private static readonly URL: string = `${environment.apiUrl}/auth/signup`;
    constructor(private tokenStore: TokenStorageService,
                private keys: DpopKeyService,
                private proofs: DpopProofService) {
        super(SignupService.URL);
    }
    
    public signup(request: SignupRequest): Observable<AuthResponse> {
        return new Observable<AuthResponse>((subscriber) => {
            (async () => {
                await this.keys.ensureKeys();
                const dpop: string = await this.proofs.buildProof('POST', SignupService.URL);
                const headers: any = {
                    'DPoP': dpop
                };
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