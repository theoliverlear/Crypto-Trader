import {Injectable} from "@angular/core";
import {Observable, of} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ErrorHandlerService {
    constructor() {
        console.log('ErrorHandlerService loaded');
    }
    handleError<T>(operation = 'operation', result?: T): (error: any) => Observable<T> {
        return (error: any): Observable<T> => {
            console.error(`${operation} failed: ${error.message}`);
            return of(result as T);
        };
    }
}