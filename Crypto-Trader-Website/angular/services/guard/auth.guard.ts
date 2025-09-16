import {Injectable} from "@angular/core";
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
    constructor(private router: Router) {
        console.log('AuthGuard loaded');
    }
    
    // TODO: Implement an auth guard with HTTP requests.
    canActivate(route: ActivatedRouteSnapshot,
                state: RouterStateSnapshot): Observable<boolean> {
        // return this.loginService.getIsLoggedInFromServer().pipe(
        //     map(isAuthorized => {
        //         if (isAuthorized) {
        //             return true;
        //         } else {
        //             this.router.navigate(['/authorize']);
        //             return false;
        //         }
        //     }),
        //     catchError(() => {
        //         this.router.navigate(['/authorize']);
        //         return new Observable<boolean>(observer => {
        //             observer.next(false);
        //         });
        //     })
        // )
        return new Observable<boolean>(observer => {
            observer.next(true);
        });
    }
}