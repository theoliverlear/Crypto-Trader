import {HttpClientService} from "@theoliverlear/angular-suite";
import {environment} from "../../../environments/environment";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CurrencyList} from "../../../models/currency/types";

@Injectable({
    providedIn: 'root'
})
export class GetAllCurrenciesService extends HttpClientService<any, CurrencyList> {
    private static readonly URL: string = `${environment.apiUrl}/currency/all`;
    constructor(httpClient: HttpClient) {
        super(GetAllCurrenciesService.URL, httpClient);
    }
    
    public getAllCurrencies(): Observable<CurrencyList> {
        return this.get();
    }
}