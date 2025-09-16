import {Injectable} from "@angular/core";
import {HttpClientService} from "@theoliverlear/angular-suite";
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class LoggedInService extends HttpClientService<any, boolean> {
    private static readonly URL: string = `${environment.apiUrl}/auth/logged-in`;
    
    constructor(httpClient: HttpClient) {
        super(LoggedInService.URL, httpClient);
    }
    
    public isLoggedIn(): Observable<boolean> {
        return this.get();
    }
}