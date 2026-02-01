import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { HttpClientService } from '@theoliverlear/angular-suite';
import { environment } from '@environments/environment';
import { DisplayCurrency } from '@models/currency/types';

@Injectable({
    providedIn: 'root',
})
export class DisplayCurrencyService extends HttpClientService<
    never,
    DisplayCurrency
> {
    private static readonly URL: string = `${environment.apiUrl}/currency/display/`;

    constructor() {
        super(DisplayCurrencyService.URL);
    }

    public getDisplayCurrency(
        currencyCode: string,
    ): Observable<DisplayCurrency> {
        this.url = `${DisplayCurrencyService.URL}${currencyCode}`;
        return this.get();
    }
}
