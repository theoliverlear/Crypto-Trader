import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { HttpClientService } from '@theoliverlear/angular-suite';
import { environment } from '@environments/environment';
import { CurrencyNames } from '@models/currency/types';

@Injectable({
    providedIn: 'root',
})
export class CurrencyNamesService extends HttpClientService<
    never,
    CurrencyNames
> {
    private static readonly URL: string = `${environment.apiUrl}/currency/list?withCode=`;
    constructor() {
        super(CurrencyNamesService.URL);
    }

    getCurrencyNames(withCode: boolean): Observable<CurrencyNames> {
        this.url = CurrencyNamesService.URL + withCode;
        return this.get();
    }
}
