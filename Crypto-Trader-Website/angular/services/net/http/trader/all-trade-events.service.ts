import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { HttpClientService } from '@theoliverlear/angular-suite';
import { environment } from '@environments/environment';
import { TradeEventList } from '@models/trader/types';

@Injectable({
    providedIn: 'root',
})
export class AllTradeEventsService extends HttpClientService<
    never,
    TradeEventList
> {
    private static URL: string = `${environment.apiUrl}/trader/events/all`;

    constructor() {
        super(AllTradeEventsService.URL);
    }

    public getAllTradeEvents(): Observable<TradeEventList> {
        return this.get();
    }
}
