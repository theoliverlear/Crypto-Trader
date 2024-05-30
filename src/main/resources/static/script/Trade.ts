export abstract class Trade {
    valueIncrease: number;
    currencyName: string;
    timeOccurred: string;
    constructor(valueIncrease: number, currencyName: string, timeOccurred: string) {
        this.valueIncrease = valueIncrease;
        this.currencyName = currencyName;
        this.timeOccurred = timeOccurred;
    }
    abstract getTradeHtml(): string;
}