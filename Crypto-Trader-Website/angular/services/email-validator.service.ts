import {Injectable} from "@angular/core";
import {Observable, Subject} from "rxjs";
import {AuthPopup} from "@theoliverlear/angular-suite";

@Injectable({
    providedIn: 'root'
})
export class EmailValidatorService {
    private validEmailSubject: Subject<AuthPopup> = new Subject();
    validEmail$: Observable<AuthPopup> = this.validEmailSubject.asObservable();
    constructor() {
        console.log('EmailValidatorService loaded');
    }
    isValidEmail(email: string): boolean {
        console.log('EmailValidatorService: isValidEmail');
        let emailPattern = new RegExp('^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$');
        return emailPattern.test(email);
    }
    emitValidEmail(isValid: boolean) {
        console.log('EmailValidatorService: emitValidEmail');
        let authPopup: AuthPopup = isValid ? null : AuthPopup.INVALID_EMAIL;
        this.validEmailSubject.next(authPopup);
    }
}