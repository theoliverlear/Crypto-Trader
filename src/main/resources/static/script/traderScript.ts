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

let tradesDiv = $('#trades-div');

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
    serverResponse.forEach((portfolioAssetHistory: any) => {
        let shares = portfolioAssetHistory.shares;
        let dollars = formatDollars(portfolioAssetHistory.assetWalletDollars);
        let valueIncrease = portfolioAssetHistory.valueChange;
        let currencyName: string = getNameByCurrencyCode(portfolioAssetHistory.currency);
        let timeOccurred: string = formatDate(portfolioAssetHistory.lastUpdated);
        let tradeType: TradeType = determineTradeType(shares);
        let trade: Trade;
        if (tradeType === TradeType.BUY) {
            trade = new BuyTrade(valueIncrease, currencyName, timeOccurred, dollars, shares);
        } else {
            trade = new SellTrade(valueIncrease, currencyName, timeOccurred, dollars, shares);
        }
        addTradeToPage(trade, tradeType);
    });
}

function determineTradeType(shareValue: number): TradeType {
    if (shareValue > 0) {
        return TradeType.BUY;
    } else {
        return TradeType.SELL;
    }
}

function addTradeToPage(trade: Trade, tradeType: TradeType) {
    let tradeDiv = document.createElement('div');
    let tradeClass = tradeType === TradeType.BUY ? 'buy-item' : 'sell-item';
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