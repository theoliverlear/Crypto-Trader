import {Trade} from "./Trade";

export class BuyTrade extends Trade {
    valueIncrease: number;
    currencyName: string;
    timeOccurred: string;
    buyValue: string;
    shareBuy: number;
    constructor(valueIncrease: number, currencyName: string, timeOccurred: string,  buyValue: string, shareBuy: number) {
        super(valueIncrease, currencyName, timeOccurred);
        this.buyValue = buyValue;
        this.shareBuy = shareBuy;
    }
    getTradeHtml(): string {
        return `
            // TODO: Change phrasing or edit functionality so initial value text is not 0.
            <h4>Value increased <span class="value-increase-text">${this.valueIncrease}</span></h4>
            <h6>Buy <span class="buy-value-text">${this.buyValue}</span> of <span class="trader-item-currency">${this.currencyName}</span> for <span class="share-buy-text">${this.shareBuy}</span> shares.</h6>
            <h6>at <span class="trade-time-text">${this.timeOccurred}</span></h6>
            `;
    }
}