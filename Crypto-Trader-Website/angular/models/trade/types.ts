import {VendorOption} from "../vendor/VendorOption";

export type TradeCheckoutRequest = {
    numDollars: number,
    numShares: number,
    currencyCode: string,
    vendor: VendorOption,
};