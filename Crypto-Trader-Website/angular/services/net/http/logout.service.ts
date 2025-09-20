import {Injectable} from "@angular/core";
import {HttpClientService} from "@theoliverlear/angular-suite";
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {AuthResponse} from "../../../models/auth/types";

@Injectable({
    providedIn: 'root'
})
export class LogoutService extends HttpClientService<any, AuthResponse> {
    private static readonly URL = `${environment.apiUrl}/auth/logout`;
    
    constructor(httpClient: HttpClient) {
        super(LogoutService.URL, httpClient);
    }
    
    public logout(): Observable<AuthResponse> {
        return this.get();
    }
}