export type Currency = {
    currencyName: string;
    currencyCode: string;
    value: number;
}

export type CurrencyList = {
    currencies: Currency[];
}