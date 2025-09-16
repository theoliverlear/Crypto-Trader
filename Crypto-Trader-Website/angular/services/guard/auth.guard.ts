import {Injectable} from "@angular/core";
import {
    ActivatedRouteSnapshot,
    CanActivate,
    Router,
    RouterStateSnapshot
} from "@angular/router";
import {catchError, map, Observable} from "rxjs";
import {LoggedInService} from "../net/http/logged-in.service";

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {
    constructor(private router: Router,
                private loggedInService: LoggedInService) {
        
    }
    
    canActivate(route: ActivatedRouteSnapshot,
                state: RouterStateSnapshot): Observable<boolean> {
        return this.loggedInService.isLoggedIn().pipe(
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