import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { environment } from "../../../environments/environment";
import { Observable, of } from "rxjs";
import { catchError, map, shareReplay } from "rxjs/operators";
import { HttpClientService } from "@theoliverlear/angular-suite";
import {
    HistoryPoint,
    ObservableHistoryMap, ObservableHistoryPoint, TimeValueResponse
} from "../../../models/currency/types";


@Injectable({
    providedIn: 'root'
})
export class GetCurrencyHistoryService extends HttpClientService<any, TimeValueResponse[] | null> {
    private static URL = `${environment.apiUrl}/currency/history`;
    private cache: ObservableHistoryMap = new Map<string, ObservableHistoryPoint>();

    constructor(httpClient: HttpClient) {
        super(GetCurrencyHistoryService.URL, httpClient);
    }

    getHistory(currencyCode: string,
               hours: number = 24,
               intervalSeconds: number = 3600): Observable<HistoryPoint[]> {
        const key: string = `${currencyCode}:${hours}:${intervalSeconds}`;
        const cached: ObservableHistoryPoint = this.cache.get(key);
        if (cached) {
            return cached;
        }
        this.url = this.getUrl(currencyCode, hours, intervalSeconds);
        const request$ = this.getCurrencyHistoryObservable();
        this.cache.set(key, request$);
        return request$;
    }

    private getCurrencyHistoryObservable() {
        const request$ = this.get().pipe(
            map((response) => (response || []).map(point => {
                return ({
                    date: new Date(point.timestamp),
                    value: point.value
                });
            })),
            catchError(() => of([])),
            shareReplay({bufferSize: 1, refCount: false})
        );
        return request$;
    }

    private getUrl(currencyCode: string,
                   hours: number,
                   intervalSeconds: number): string {
        let params: string = `${currencyCode}?hours=${hours}&intervalSeconds=${intervalSeconds}`;
        return `${GetCurrencyHistoryService.URL}/${params}`;
    }
}