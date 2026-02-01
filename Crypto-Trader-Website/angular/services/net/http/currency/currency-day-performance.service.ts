import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { HttpClientService } from '@theoliverlear/angular-suite';
import { environment } from '@environments/environment';
import { PerformanceRating } from '@models/currency/types';

@Injectable({
    providedIn: 'root',
})
export class CurrencyDayPerformanceService extends HttpClientService<
    never,
    PerformanceRating
> {
    private static readonly URL: string = `${environment.apiUrl}/currency/performance/`;

    constructor() {
        super(CurrencyDayPerformanceService.URL);
    }

    public getCurrencyDayPerformance(
        currencyCode: string,
    ): Observable<PerformanceRating> {
        this.url = `${CurrencyDayPerformanceService.URL}${currencyCode}`;
        return this.get();
    }
}
