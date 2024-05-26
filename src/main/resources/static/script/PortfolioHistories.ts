export class PortfolioHistories {
    //============================-Variables-=================================
    totalValues: number[];
    dates: string[];
    //===========================-Constructors-===============================
    constructor() {
        this.totalValues = [];
        this.dates = [];
    }
    //=============================-Methods-==================================
    addTotalValueEntry(totalValue: number, date: string): void {
        this.totalValues.push(totalValue);
        this.dates.push(date);
    }
    //=============================-Getters-==================================

    //-----------------------Add-Total-Value-Entry----------------------------
    get getTotalValues(): number[] {
        return this.totalValues;
    }
    get getDates(): string[] {
        return this.dates;
    }
}