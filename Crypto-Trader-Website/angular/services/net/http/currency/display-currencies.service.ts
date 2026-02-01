import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { HttpClientService } from '@theoliverlear/angular-suite';
import { environment } from '@environments/environment';
import { DisplayCurrencyList } from '@models/currency/types';

@Injectable({
    providedIn: 'root',
})
export class DisplayCurrenciesService extends HttpClientService<
    never,
    DisplayCurrencyList
> {
    private static readonly URL: string = `${environment.apiUrl}/currency/all`;
    constructor() {
        super(DisplayCurrenciesService.URL);
    }

    public getAllCurrencies(): Observable<DisplayCurrencyList> {
        return this.get();
    }

    public getAllCurrenciesWithOffset(
        offset: number,
    ): Observable<DisplayCurrencyList> {
        this.url = `${DisplayCurrenciesService.URL}?offset=${offset}`;
        return this.get();
    }
}
