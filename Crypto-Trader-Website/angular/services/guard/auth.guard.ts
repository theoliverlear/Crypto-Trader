import {Injectable} from "@angular/core";
import {LoginService} from "../server/login.service";
import {
    ActivatedRouteSnapshot,
    CanActivate,
    Router,
    RouterStateSnapshot
} from "@angular/router";
import {catchError, map, Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {
    constructor(private loginService: LoginService,
                private router: Router) {
        console.log('AuthGuard loaded');
    }
    canActivate(route: ActivatedRouteSnapshot,
                state: RouterStateSnapshot): Observable<boolean> {
        return this.loginService.getIsLoggedInFromServer().pipe(
            map(isAuthorized => {
                if (isAuthorized) {
                    return true;
                } else {
                    this.router.navigate(['/authorize']);
                    return false;
                }
            }),
            catchError(() => {
                this.router.navigate(['/authorize']);
                return new Observable<boolean>(observer => {
                    observer.next(false);
                });
            })
        )
    }
}