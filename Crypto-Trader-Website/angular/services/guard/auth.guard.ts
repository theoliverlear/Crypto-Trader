import {Injectable} from "@angular/core";
import {
    ActivatedRouteSnapshot,
    CanActivate,
    Router,
    RouterStateSnapshot
} from "@angular/router";
import {catchError, map, Observable} from "rxjs";
import {LoggedInService} from "../net/http/logged-in.service";
import {TokenStorageService} from "../auth/token-storage.service";

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {
    constructor(private router: Router,
                private loggedInService: LoggedInService,
                private tokenStorageService: TokenStorageService) {
        
    }
    
    canActivate(route: ActivatedRouteSnapshot,
                state: RouterStateSnapshot): Observable<boolean> {
        return this.loggedInService.isLoggedIn().pipe(
            map(authResponse => {
                console.log('AuthGuard: isAuthorized', authResponse);
                if (authResponse.authorized) {
                    return true;
                } else {
                    this.router.navigate(['/authorize']).then(navigated => {
                        return false;
                    });
                }
            }),
            catchError(() => {
                this.router.navigate(['/authorize']).then(navigated => {
                    
                });
                return new Observable<boolean>(observer => {
                    observer.next(false);
                });
            })
        )
    }
}