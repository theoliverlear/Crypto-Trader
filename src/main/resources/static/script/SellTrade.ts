import {Trade} from "./Trade";

export class SellTrade extends Trade {
    valueIncrease: string;
    shareOrWalletChange: string;
    currencyName: string;
    timeOccurred: string;
    sellShares: number;
    sellValue: string;
    constructor(valueIncrease: string, shareOrWalletChange: string,
                currencyName: string, timeOccurred: string, sellValue: string,
                sellShares: number) {
        super(valueIncrease, shareOrWalletChange, currencyName, timeOccurred);
        this.sellValue = sellValue;
        this.sellShares = sellShares;
    }
    getTradeHtml(): string {
        return `
            <h4>Value increased <span class="value-increase-text">${this.valueIncrease}</span></h4>
            <h6>Sell <span class="sell-shares-text">${this.shareOrWalletChange}</span> shares of <span class="trader-item-currency">${this.currencyName}</span> for <span class="sell-value-text">${this.sellValue}</span>.</h6>
            <h6>at <span class="trade-time-text">${this.timeOccurred}</span></h6>            
            `;
    }
}