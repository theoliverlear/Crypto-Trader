import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { HttpClientService } from '@theoliverlear/angular-suite';
import { environment } from '@environments/environment';
import { TradeEventList } from '@models/trader/types';

@Injectable({
    providedIn: 'root',
})
export class BatchedTradeEventsService extends HttpClientService<
    never,
    TradeEventList
> {
    private static readonly URL: string = `${environment.apiUrl}/trader/events/batch`;
    constructor() {
        super(BatchedTradeEventsService.URL);
    }

    public getBatchedTradeEvents(
        offset: number,
        limit: number,
    ): Observable<TradeEventList> {
        this.url = `${BatchedTradeEventsService.URL}?offset=${offset}&limit=${limit}`;
        return this.get();
    }
}
