import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { HttpClientService } from '@theoliverlear/angular-suite';
import { environment } from '@environments/environment';

@Injectable({
    providedIn: 'root',
})
export class HasTradeEventsService extends HttpClientService<never, boolean> {
    private static readonly URL: string = `${environment.apiUrl}/trader/events/exists`;
    constructor() {
        super(HasTradeEventsService.URL);
    }

    public hasTradeEvents(): Observable<boolean> {
        return this.get();
    }
}
