import {Injectable} from "@angular/core";
import {Observable, Subject} from "rxjs";
import {AuthPopup} from "../models/auth/AuthPopup";

@Injectable({
    providedIn: 'root'
})
export class FilledFieldsService {
    private filledFieldsSubject: Subject<AuthPopup> = new Subject();
    fieldsFilled$: Observable<AuthPopup> = this.filledFieldsSubject.asObservable();
    constructor() {
        console.log('FilledFieldsService loaded');
    }
    isFilledFields(fields: string[]): boolean {
        for (let field of fields) {
            if (!this.isFilledField(field)) {
                return false;
            }
        }
        return true;
    }
    isFilledField(field: string): boolean {
        return field.length > 0;
    }
    emitFilledFields(authPopup: AuthPopup) {
        this.filledFieldsSubject.next(authPopup);
    }
}