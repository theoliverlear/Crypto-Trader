import {Injectable} from "@angular/core";
import {HttpClientService} from "@theoliverlear/angular-suite";
import {CurrencyNames} from "../../../../models/currency/types";
import {environment} from "../../../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class CurrencyNamesService extends HttpClientService<any, CurrencyNames> {
    private static readonly URL: string = `${environment.apiUrl}/currency/list?withCode=`;
    constructor() {
        super(CurrencyNamesService.URL);
    }
    
    getCurrencyNames(withCode: boolean): Observable<CurrencyNames> {
        this.url = CurrencyNamesService.URL + withCode;
        return this.get();
    }
}