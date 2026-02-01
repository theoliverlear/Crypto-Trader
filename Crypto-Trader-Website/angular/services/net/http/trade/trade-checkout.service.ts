import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { HttpClientService } from '@theoliverlear/angular-suite';
import { environment } from '@environments/environment';
import { TradeCheckoutRequest } from '@models/trade/types';
import { OperationSuccessResponse } from '@models/types';

@Injectable({
    providedIn: 'root',
})
export class TradeCheckoutService extends HttpClientService<
    TradeCheckoutRequest,
    OperationSuccessResponse
> {
    private static readonly URL: string = `${environment.apiUrl}/trade/checkout`;
    constructor() {
        super(TradeCheckoutService.URL);
    }

    public checkout(
        request: TradeCheckoutRequest,
    ): Observable<OperationSuccessResponse> {
        return this.post(request);
    }
}
