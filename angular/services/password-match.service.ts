import {Injectable} from "@angular/core";
import {Observable, Subject} from "rxjs";
import {AuthPopup} from "../models/auth/AuthPopup";

@Injectable({
    providedIn: 'root'
})
export class PasswordMatchService {
    private passwordMismatchSubject: Subject<AuthPopup> = new Subject();
    passwordMismatch$: Observable<AuthPopup> = this.passwordMismatchSubject.asObservable();
    constructor() {
        console.log('PasswordMatchHandlerService loaded');
    }
    isMismatchPassword(password: string, confirmPassword: string): boolean {
        return password !== confirmPassword;
    }
    emitPasswordMismatch(authPopup: AuthPopup) {
        this.passwordMismatchSubject.next(authPopup);
    }
}