// const websocket = new WebSocket('ws://localhost:8080/websocket/trader');

import {Trade} from "./Trade";
import {TradeType} from "./TradeType";
import {SellTrade} from "./SellTrade";
import {BuyTrade} from "./BuyTrade";
import {
    formatDate,
    formatDollars,
    getNameByCurrencyCode,
    loadPage
} from "./globalScript";

let tradesDiv: JQuery<HTMLElement> = $('#trades-div');

async function getPortfolioAssetHistoryFromServer(): Promise<any> {
    let response = await fetch('/portfolio/history/get/asset', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.json()).catch(error => {
        console.error('Error: ', error);
    });
    console.log(response);
    return response;
}

function serverResponseToTrades(serverResponse: any): void {
    for (let i: number = serverResponse.length - 1; i >= 0; i--){
        const portfolioAssetHistory: any = serverResponse[i];
        let tradeOccurred: boolean = Boolean(portfolioAssetHistory.tradeOccurred);
        console.log(tradeOccurred);
        if (tradeOccurred) {
            console.log('showing trade');
            let shares = portfolioAssetHistory.shares;
            let dollars: string = formatDollars(portfolioAssetHistory.assetWalletDollars);
            let totalValue: string = formatDollars(String(portfolioAssetHistory.totalValueInDollars), 8);
            let shareChange: string = String(Math.abs(portfolioAssetHistory.sharesChange));
            let valueIncrease: string = formatDollars(portfolioAssetHistory.valueChange, 8);
            let currencyName: string = getNameByCurrencyCode(portfolioAssetHistory.currency);
            let timeOccurred: string = formatDate(portfolioAssetHistory.lastUpdated);
            let tradeType: TradeType = determineTradeType(shares);
            let trade: Trade;
            if (tradeType === TradeType.BUY) {
                trade = new BuyTrade(valueIncrease, totalValue, currencyName, timeOccurred, dollars, shares);
            } else {
                trade = new SellTrade(valueIncrease, shareChange, currencyName, timeOccurred, dollars, shares);
            }
            addTradeToPage(trade, tradeType);
        }
    }
}

function determineTradeType(shareValue: number): TradeType {
    if (shareValue > 0) {
        return TradeType.BUY;
    } else {
        return TradeType.SELL;
    }
}

function addTradeToPage(trade: Trade, tradeType: TradeType): void {
    let tradeDiv: HTMLDivElement = document.createElement('div');
    let tradeClass: string = tradeType === TradeType.BUY ? 'buy-item' : 'sell-item';
    tradeDiv.classList.add('trade-item');
    tradeDiv.classList.add(tradeClass);
    tradeDiv.innerHTML = trade.getTradeHtml();
    tradesDiv.append(tradeDiv);
    tradeDiv.append(document.createElement('hr'));
}
function loadTrades(): void {
    getPortfolioAssetHistoryFromServer().then(serverResponse => {
        serverResponseToTrades(serverResponse);
    });
}
if (loadPage(document.body, 'trader')) {
    loadTrades();
}