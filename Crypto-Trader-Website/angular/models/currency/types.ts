import {Observable} from "rxjs";

export type Currency = {
    currencyName: string;
    currencyCode: string;
    value: number;
};

export type CurrencyList = {
    currencies: Currency[];
};

export type DisplayCurrency = Currency & {logoUrl: string};

export type DisplayCurrencyList = {
    currencies: DisplayCurrency[];
};

export type PerformanceRating = 'up' | 'down' | 'neutral';

export type PerformanceRatingResponse = {
    rating: PerformanceRating;
};

export type HistoryPoint = {
    date: Date; value: number
};
export type TimeValueResponse = {
    timestamp: string; value: number
};
export type ObservableHistoryPoint = Observable<HistoryPoint[]>;
export type ObservableHistoryMap = Map<string, ObservableHistoryPoint>;