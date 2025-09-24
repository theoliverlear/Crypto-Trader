import {HttpClientService} from "@theoliverlear/angular-suite";
import {environment} from "../../../../environments/environment";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {
    DisplayCurrencyList
} from "../../../../models/currency/types";

@Injectable({
    providedIn: 'root'
})
export class GetAllCurrenciesService extends HttpClientService<any, DisplayCurrencyList> {
    private static readonly URL: string = `${environment.apiUrl}/currency/all`;
    constructor() {
        super(GetAllCurrenciesService.URL);
    }
    
    public getAllCurrencies(): Observable<DisplayCurrencyList> {
        return this.get();
    }
    
    public getAllCurrenciesWithOffset(offset: number): Observable<DisplayCurrencyList> {
        this.url = `${GetAllCurrenciesService.URL}?offset=${offset}`;
        return this.get();
    }
}