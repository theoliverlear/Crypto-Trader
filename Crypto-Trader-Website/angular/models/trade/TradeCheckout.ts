import {TradeCheckoutRequest} from "./types";
import {VendorOption} from "../vendor/VendorOption";

export class TradeCheckout {
    private _numDollars: number;
    private _numShares: number;
    private _currencyCode: string;
    private _vendor: VendorOption;
    constructor(numDollars: number = 0,
                numShares: number = 0,
                currencyCode: string = '',
                vendor: VendorOption = VendorOption.PAPER_MODE) {
        this._numDollars = numDollars;
        this._numShares = numShares;
        this._currencyCode = currencyCode;
        this._vendor = vendor;
    }

    get currencyCode(): string {
        return this._currencyCode;
    }

    set currencyCode(value: string) {
        this._currencyCode = value;
    }
    get numShares(): number {
        return this._numShares;
    }

    set numShares(value: number) {
        this._numShares = value;
    }
    get numDollars(): number {
        return this._numDollars;
    }

    set numDollars(value: number) {
        this._numDollars = value;
    }
    
    get vendor(): VendorOption {
        return this._vendor;
    }
    
    set vendor(value: VendorOption) {
        this._vendor = value;
    }
    
    allFilledFields(): boolean {
        const hasSharesOrDollars: boolean = this._numShares > 0 || this._numDollars > 0;
        const hasCurrencyCode: boolean = this._currencyCode.length > 0;
        return hasSharesOrDollars && hasCurrencyCode;
    }
    
    getRequest(): TradeCheckoutRequest {
        if (!this.allFilledFields()) {
            throw new Error('Not all fields are filled.');
        }
        return {
            numDollars: this._numDollars,
            numShares: this._numShares,
            currencyCode: this._currencyCode,
            vendor: this._vendor,
        };
    }
}