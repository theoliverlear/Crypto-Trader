import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ErrorHandlerService} from "../error-handler.service";
import {HashPasswordService} from "../hash-password.service";
import {httpOptions} from "./httpProperties";
import {catchError, map, Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class LoginService {
    static readonly LOGIN_URL: string = 'http://localhost:8080/api/authorize/login';
    static readonly IS_LOGGED_IN_URL: string = 'http://localhost:8080/api/auth/isloggedin';
    constructor(private http: HttpClient,
                private errorHandlerService: ErrorHandlerService,
                private hashPasswordService: HashPasswordService) {
        console.log('LoginService loaded');
    }
    sendLoginToServer(username: string, unhashedPassword: string): Observable<boolean> {
        const hashedPassword: string = this.hashPasswordService.hashPassword(unhashedPassword);
        const loginInfo = {
            username: username,
            password: hashedPassword
        };
        return this.http.post<{isAuthorized: boolean}>(LoginService.LOGIN_URL, loginInfo, httpOptions)
            .pipe(
                map(response => {
                    return response.isAuthorized;
                }),
                catchError(this.errorHandlerService.handleError('loginToServer', false))
            )
    }
    getIsLoggedInFromServer() {
        return this.http.get<{isAuthorized: boolean}>(LoginService.IS_LOGGED_IN_URL, httpOptions)
            .pipe(
                map(response => {
                    return response.isAuthorized;
                }),
                catchError(this.errorHandlerService.handleError('getIsLoggedInFromServer'))
            )
    }
}