import {Trade} from "./Trade";

export class BuyTrade extends Trade {
    valueIncrease: string;
    shareOrWalletChange: string;
    currencyName: string;
    timeOccurred: string;
    buyValue: string;
    shareBuy: number;
    constructor(valueIncrease: string, shareOrWalletChange: string, currencyName: string, timeOccurred: string, buyValue: string, shareBuy: number) {
        super(valueIncrease, shareOrWalletChange, currencyName, timeOccurred);
        this.buyValue = buyValue;
        this.shareBuy = shareBuy;
    }
    getTradeHtml(): string {
        return `
            <h4>Value increased <span class="value-increase-text">${this.valueIncrease}</span></h4>
            <h6>Buy <span class="buy-value-text">${this.shareOrWalletChange}</span> of <span class="trader-item-currency">${this.currencyName}</span> for <span class="share-buy-text">${this.shareBuy}</span> shares.</h6>
            <h6><span class="trade-time-text">${this.timeOccurred}</span></h6>
            `;
    }
}