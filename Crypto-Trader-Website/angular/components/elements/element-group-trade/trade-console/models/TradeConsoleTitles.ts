import {TradeConsoleTitle} from "./types";

export class TradeConsoleTitles {
    public static readonly VENDOR: TradeConsoleTitle = {
        title: "Select a vendor",
        subtitle: "Recommended: always start in paper mode."
    };
    public static readonly CURRENCY: TradeConsoleTitle = {
        title: "Select a currency",
        subtitle: "Choose from over 300+ cryptocurrencies."
    }
    public static readonly ORDERED_TITLES = [
        TradeConsoleTitles.VENDOR,
        TradeConsoleTitles.CURRENCY
    ]
    public static getByPage(page: number): TradeConsoleTitle {
        if (page < 0 || page >= TradeConsoleTitles.ORDERED_TITLES.length) {
            throw new Error("Invalid page number");
        }
        return TradeConsoleTitles.ORDERED_TITLES[page - 1];
    }
}