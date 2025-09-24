import {
    ActivatedRouteSnapshot,
    CanActivate,
    GuardResult,
    MaybeAsync, Router,
    RouterStateSnapshot
} from "@angular/router";
import {catchError, map, Observable, Subject} from "rxjs";
import {LoggedInService} from "../net/http/auth/status/logged-in.service";
import {Injectable} from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class AccountGuard implements CanActivate {
    accountRedirect$: Subject<undefined> = new Subject<undefined>();
    
    constructor(private loggedInService: LoggedInService,
                private router: Router) {
        
    }
    
    public getAccountRedirect(): Subject<undefined> {
        return this.accountRedirect$;
    }
    
    canActivate(route: ActivatedRouteSnapshot,
                state: RouterStateSnapshot): MaybeAsync<GuardResult> {
        return this.loggedInService.isLoggedIn().pipe(
            map(authResponse => {
                if (authResponse.authorized) {
                    this.accountRedirect$.next(undefined);
                    this.router.navigate(['/account']).then(navigated => {
                        return false;
                    })
                } else {
                    return true;
                }
            }),
            catchError(() => {
                this.router.navigate(['/authorize']).then(navigated => {

                });
                return new Observable<boolean>(observer => {
                    observer.next(false);
                });
            })
        );
    }
}