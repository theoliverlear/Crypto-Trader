export abstract class Trade {
    valueIncrease: string;
    shareOrWalletChange: string;
    currencyName: string;
    timeOccurred: string;
    constructor(valueIncrease: string, shareOrWalletChange: string, currencyName: string, timeOccurred: string) {
        this.valueIncrease = valueIncrease;
        this.shareOrWalletChange = shareOrWalletChange;
        this.currencyName = currencyName;
        this.timeOccurred = timeOccurred;
    }
    abstract getTradeHtml(): string;
}