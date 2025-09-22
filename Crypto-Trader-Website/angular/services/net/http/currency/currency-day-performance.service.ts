import {Injectable} from "@angular/core";
import {HttpClientService} from "@theoliverlear/angular-suite";
import {PerformanceRating} from "../../../../models/currency/types";
import {environment} from "../../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class CurrencyDayPerformanceService extends HttpClientService<any, PerformanceRating> {
    private static readonly URL: string = `${environment.apiUrl}/currency/performance/`;
    
    constructor(httpClient: HttpClient) {
        super(CurrencyDayPerformanceService.URL, httpClient);
    }
    
    public getCurrencyDayPerformance(currencyCode: string): Observable<PerformanceRating> {
        this.url = `${CurrencyDayPerformanceService.URL}${currencyCode}`;
        return this.get();
    }
}