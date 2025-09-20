import {Injectable} from "@angular/core";
import {HttpClientService} from "@theoliverlear/angular-suite";
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable, map} from "rxjs";
import {AuthResponse} from "../../../models/auth/types";
import {TokenStorageService} from "../../auth/token-storage.service";

@Injectable({
    providedIn: 'root'
})
export class LoggedInService extends HttpClientService<any, AuthResponse> {
    private static readonly URL: string = `${environment.apiUrl}/auth/logged-in`;
    
    constructor(httpClient: HttpClient, private tokenStorage: TokenStorageService) {
        super(LoggedInService.URL, httpClient);
    }
    
    public isLoggedIn(): Observable<AuthResponse> {
        return this.get().pipe(
            map((response: AuthResponse) => {
                if (response?.authorized && !response.token) {
                    const token: string = this.tokenStorage.getToken();
                    return token ? { ...response, token } : response;
                }
                return response;
            })
        );
    }
}