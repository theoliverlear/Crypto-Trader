export type TradeEvent = {
    id: number;
    currency: string;
    valueChange: number;
    sharesChange: number;
    tradeTime: string;
    tradeType: string;
    vendor: string;
};

export type TradeEventList = {
    events: TradeEvent[];
};
