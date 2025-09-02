import {Injectable} from "@angular/core";
import {HttpClientService} from "@theoliverlear/angular-suite";
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

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

export type HistoryPoint = {
    date: Date; value: number
};
export type TimeValueResponse = {
    timestamp: string; value: number
};
export type ObservableHistoryPoint = Observable<HistoryPoint[]>;
export type ObservableHistoryMap = Map<string, ObservableHistoryPoint>;