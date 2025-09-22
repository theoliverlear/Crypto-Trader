import {Injectable} from "@angular/core";
import {
    ActivatedRouteSnapshot,
    CanActivate,
    Router,
    RouterStateSnapshot
} from "@angular/router";
import {catchError, map, Observable} from "rxjs";
import {LoggedInService} from "../net/http/auth/status/logged-in.service";
import {TokenStorageService} from "../auth/token-storage.service";

@Injectable({
    providedIn: 'root'
})
/**
 * Route guard that checks server-side auth status before activating a protected route.
 *
 * Behavior:
 * - Calls LoggedInService.isLoggedIn() (GET /auth/logged-in) to determine whether the current request is authenticated.
 * - If authorized, allows activation. Otherwise, redirects to /authorize and blocks activation.
 * - It does not attach Authorization/DPoP itself; the endpoint is designed to be public and reflect state.
 */
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