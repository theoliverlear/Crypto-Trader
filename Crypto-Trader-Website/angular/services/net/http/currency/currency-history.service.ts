import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, map, shareReplay } from 'rxjs/operators';

import { HttpClientService } from '@theoliverlear/angular-suite';
import { environment } from '@environments/environment';
import {
    HistoryPoint,
    ObservableHistoryMap,
    ObservableHistoryPoint,
    TimeValueResponse,
} from '@models/currency/types';

/** Provides a history of currency prices.
 *
 */
@Injectable({
    providedIn: 'root',
})
export class CurrencyHistoryService extends HttpClientService<
    never,
    TimeValueResponse[] | null
> {
    private static readonly URL: string = `${environment.apiUrl}/currency/history`;
    private readonly cache: ObservableHistoryMap = new Map<
        string,
        ObservableHistoryPoint
    >();

    constructor() {
        super(CurrencyHistoryService.URL);
    }

    /** Gets the price history for the specified currency.
     *
     * @param currencyCode
     * @param hours
     * @param intervalSeconds
     * @returns An observable of price history points.
     */
    getHistory(
        currencyCode: string,
        hours: number = 24,
        intervalSeconds: number = 3600,
    ): Observable<HistoryPoint[]> {
        const key: string = `${currencyCode}:${hours}:${intervalSeconds}`;
        const cached: ObservableHistoryPoint | undefined = this.cache.get(key);
        if (cached) {
            return cached;
        }
        this.url = this.getUrl(currencyCode, hours, intervalSeconds);
        const request$: Observable<HistoryPoint[]> = this.getCurrencyHistoryObservable();
        this.cache.set(key, request$);
        return request$;
    }

    private getCurrencyHistoryObservable(): Observable<HistoryPoint[]> {
        const request$: Observable<HistoryPoint[]> = this.get().pipe(
            map((response): HistoryPoint[] =>
                (response || []).map((point): HistoryPoint => {
                    return {
                        date: new Date(point.timestamp),
                        value: point.value,
                    };
                }),
            ),
            catchError((): Observable<HistoryPoint[]> => of([])),
            shareReplay({ bufferSize: 1, refCount: false }),
        );
        return request$;
    }

    private getUrl(
        currencyCode: string,
        hours: number,
        intervalSeconds: number,
    ): string {
        const params: string = `${currencyCode}?hours=${hours}&intervalSeconds=${intervalSeconds}`;
        return `${CurrencyHistoryService.URL}/${params}`;
    }
}
