import {Injectable} from "@angular/core";
import {Observable, Subject} from "rxjs";
import {AuthPopup} from "@theoliverlear/angular-suite";

@Injectable({
    providedIn: 'root'
})
export class PasswordMatchService {
    private passwordMismatchSubject: Subject<AuthPopup> = new Subject();
    passwordMismatch$: Observable<AuthPopup> = this.passwordMismatchSubject.asObservable();
    constructor() {
        
    }
    isMismatchPassword(password: string, confirmPassword: string): boolean {
        return password !== confirmPassword;
    }
    emitPasswordMismatch(authPopup: AuthPopup): void {
        this.passwordMismatchSubject.next(authPopup);
    }
}