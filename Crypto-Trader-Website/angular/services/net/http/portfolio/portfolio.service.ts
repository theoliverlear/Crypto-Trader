import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { HttpClientService } from '@theoliverlear/angular-suite';
import { environment } from '@environments/environment';
import { Portfolio } from '@models/portfolio/types';

@Injectable({
    providedIn: 'root',
})
export class PortfolioService extends HttpClientService<never, Portfolio> {
    private static readonly URL: string = `${environment.apiUrl}/portfolio/get`;
    constructor() {
        super(PortfolioService.URL);
    }
    getPortfolio(): Observable<Portfolio> {
        return this.get();
    }
}
