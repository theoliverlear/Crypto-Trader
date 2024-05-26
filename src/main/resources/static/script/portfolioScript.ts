//=================================-Imports-==================================
import {
    defaultCurrencyImage, formatDollars, getCodeByCurrencyName,
    getCurrencyLogoFromName, loadPage, sanitizeString
} from "./globalScript.js";
import {PortfolioAsset} from "./PortfolioAsset.js";
import {PortfolioView} from "./PortfolioView";
import {
    buildPortfolioPieChart,
    buildPortfolioTotalValueHistoryChart
} from "./graphScript";
import {PortfolioHistories} from "./PortfolioHistories";
//================================-Variables-=================================

//-----------------------------------Inputs-----------------------------------
let sharesInput: HTMLElement = document.getElementById('shares-input');
let walletInput: HTMLElement = document.getElementById('wallet-input');
let inputs: HTMLElement[] = [sharesInput, walletInput];
let inputArray: HTMLElement[] = Array.from(inputs);
//---------------------------------Containers---------------------------------
const addCurrencyDiv: HTMLElement = document.getElementById('add-currency-div');
const emptyPortfolioSection: HTMLElement = document.getElementById('empty-portfolio-section');
let yourCurrenciesListSection: HTMLElement = document.getElementById('your-currencies-list-section');
//-----------------------------Currency-Selection-----------------------------
const currencySelectionText: HTMLElement = document.getElementById('currency-selection-text');
const currencySelectionOptions: HTMLCollectionOf<Element> = document.getElementsByClassName('currency-dropdown-option');
const currencySelectionOptionsArray: Element[] = Array.from(currencySelectionOptions);
let selectedCurrencyText: HTMLElement = document.getElementById('selected-currency-text');
let selectedCurrencyImage: HTMLElement = document.getElementById('your-currency-image');
//------------------------------Currency-Options------------------------------
let currencyOptionsDropdown = document.getElementsByClassName('currency-dropdown-option');
let currencyOptionsArray = Array.from(currencyOptionsDropdown);
const currencyDropdownSelection: HTMLElement = document.getElementById('currency-dropdown-selection');
//-------------------------------Dropdown-Caret-------------------------------
const currencyDropdownCaret: JQuery<HTMLElement> = $('#currency-dropdown-caret');
const currencyDropdownCaretImage: JQuery<HTMLElement> = $('#currency-dropdown-caret-image');
//-----------------------------------Popup------------------------------------
let popupDiv: HTMLElement = document.getElementById('popup-div');
let popupText: HTMLElement = document.getElementById('popup-text');
//----------------------------------Buttons-----------------------------------
let addButton: HTMLElement = document.getElementById('add-button');
const cancelButton: HTMLElement = document.getElementById('cancel-button');
const addCurrencyButton: HTMLElement = document.getElementById('add-currency-button');
const currencyDropdownButton: HTMLElement = document.getElementById('currency-dropdown');
//-------------------------------Portfolio-View-------------------------------
let portfolioSelectorOption: JQuery<HTMLElement> = $('.portfolio-selector-option');
let currentView: PortfolioView = PortfolioView.PORTFOLIO_VIEW;
//------------------------------Performance-View------------------------------
let portfolioSection: JQuery<HTMLElement> = $('#portfolio-section');
let performanceSection: JQuery<HTMLElement> = $('#performance-section');
let performanceChart: HTMLElement = $('#performance-chart')[0];
//==================================-Types-===================================

//------------------------------Simple-Currency-------------------------------
type SimpleCurrency = {
    name: string,
    currencyCode: string
}
//---------------------------Simple-Portfolio-Asset---------------------------
type SimplePortfolioAsset = {
    currency: SimpleCurrency,
    shares: number,
    assetWalletDollars: string,
    totalValueInDollars: string
}
//=============================-Server-Functions-=============================

//-------------------------Get-Portfolio-From-Server--------------------------
// TODO: Either have a WebSocket updating their portfolio in real-time or have
//       a refresh button that updates the page.
async function getPortfolioFromServer(): Promise<any> {
    let response = await fetch('/portfolio/get', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
    let portfolio = await response.json();
    return portfolio;
}
//-----------------------Send-Portfolio-Asset-To-Server-----------------------
function sendPortfolioAssetToServer(): void {
    if (hasSelectedCurrency() && sharesOrWalletHaveInput()) {
        let currencyName: string = sanitizeString(selectedCurrencyText.textContent);
        let shares: string | number = initializeEmptyWalletShares((sharesInput as HTMLInputElement).value);
        let wallet: string | number = initializeEmptyWalletShares((walletInput as HTMLInputElement).value);
        fetch('/portfolio/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                currencyName: currencyName,
                shares: shares,
                walletDollars: wallet
            })
        })
    }
}
//---------------------Get-Is-Empty-Portfolio-From-Server---------------------
async function getIsEmptyPortfolioFromServer(): Promise<boolean> {
    let response: Response = await fetch('/portfolio/empty', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
    let responseJson: string = await response.text();
    return responseJson === 'true';
}
//-------------------Get-Portfolio-Asset-Profit-From-Server-------------------
async function getPortfolioAssetProfitFromServer(currencyName: string): Promise<any> {
    let response = await fetch(`/portfolio/history/profit/${currencyName}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
    let profit = await response.json();
    return profit.value;
}
//---------------------Get-Portfolio-History-From-Server----------------------
async function getPortfolioHistoryFromServer(): Promise<any> {
    let response = await fetch('/portfolio/history/get', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
    let portfolioHistory = await response.json();
    return portfolioHistory;
}
//=============================-Client-Functions-=============================

//---------------------------Select-Portfolio-View----------------------------
function selectPortfolioView(): void {
    portfolioSelectorOption.not(this).removeClass('selected-portfolio-option').fadeTo(200, 1);
    $(this).fadeTo(200, 0, function(): void {
        $(this).addClass('selected-portfolio-option').fadeTo(200, 1);
    });
    setPortfolioView(this);
}
//----------------------------Set-Portfolio-View------------------------------
function setPortfolioView(element: HTMLElement): void {
    if (element.id === PortfolioView.PORTFOLIO_VIEW) {
        currentView = PortfolioView.PORTFOLIO_VIEW;
        portfolioSection.fadeIn(200);
        performanceSection.fadeOut(200);
    } else {
        currentView = PortfolioView.PERFORMANCE_VIEW;
        portfolioSection.fadeOut(200);
        performanceSection.fadeIn(200, function(): void {
            performanceSection.css('display', 'flex');
        });
        displayDefaultGraph();
    }
}
//----------------------------Display-Default-Graph---------------------------
async function displayDefaultGraph(): Promise<void> {
    let portfolioHistories: PortfolioHistories = new PortfolioHistories();
    const portfolioHistory = await getPortfolioHistoryFromServer();
    if (Array.isArray(portfolioHistory)) {
        portfolioHistory.forEach((history: any): void => {
            portfolioHistories.addTotalValueEntry(history.totalWorth, history.lastUpdated);
        });
    }
    buildPortfolioTotalValueHistoryChart((performanceChart as HTMLCanvasElement), portfolioHistories);
}
//----------------------------Load-Empty-Container----------------------------
function showCorrectContainer(): void {
    getIsEmptyPortfolioFromServer().then((isEmpty: boolean): void => {
        if (isEmpty) {
            emptyPortfolioSection.style.display = 'flex';
            yourCurrenciesListSection.style.display = 'none';
        } else {
            emptyPortfolioSection.style.display = 'none';
            yourCurrenciesListSection.style.display = 'flex';
            loadCurrencies();
        }
    });
}
//------------------------------Load-Currencies-------------------------------
async function loadCurrencies(): Promise<void> {
    yourCurrenciesListSection.innerHTML = '';
    getPortfolioFromServer().then(async portfolio => {
        if (Array.isArray(portfolio.assets)) {
            for (const asset of portfolio.assets) {
                let currencyName: string = asset.currency.name;
                let currencyCode: string = asset.currency.currencyCode;
                let shares = asset.shares;
                let walletDollars: string = formatDollars(asset.assetWalletDollars);
                let totalAssetValue: string = formatDollars(asset.totalValueInDollars);
                let allTimeProfit: number = await getPortfolioAssetProfitFromServer(currencyName);
                let allTimeProfitString: string = formatDollars(String(allTimeProfit));
                addCurrencyToPage(currencyName, currencyCode, shares, walletDollars, totalAssetValue, allTimeProfitString);
            }
        }
    });
}
//----------------------------Add-Currency-To-Page----------------------------
function addCurrencyToPage(currencyName: string,
                           currencyCode: string,
                           shares: number,
                           walletDollars: string,
                           totalAssetValue: string,
                           allTimeProfit: string): void {
    let currencyImageSrc: string = getCurrencyLogoFromName(currencyName);
    let currencyAsset: PortfolioAsset = new PortfolioAsset(currencyName,
                                                           currencyCode,
                                                           shares,
                                                           walletDollars,
                                                           totalAssetValue,
                                                           allTimeProfit,
                                                           currencyImageSrc);
    let currencyDiv: HTMLDivElement = document.createElement('div');
    currencyDiv.classList.add('simple-space-inline-div');
    currencyDiv.classList.add('your-currency-item');
    let currencyHtml: string = currencyAsset.buildHtml();
    currencyDiv.innerHTML = currencyHtml;
    yourCurrenciesListSection.appendChild(currencyDiv);
}
//-----------------------------Add-Item-Sequence------------------------------
async function addItemSequence(): Promise<void> {
    if (sharesAndWalletAreEmpty()) {
        showPopup('Please fill out either the shares or wallet field.');
        return;
    }
    if (bothSharesAndWalletHaveInput()) {
        showPopup('Both shares and wallet have input. Please only input one.');
        return;
    }
    sendPortfolioAssetToServer();
    clearInputs();
    hideCurrencyDropdown();
    hideAddCurrencyDiv();
    showCorrectContainer();
}
//--------------------------------Limit-Input---------------------------------
function limitInput(): void {
    if (this.value >= 10_000_000) {
        this.value = 10_000_000;
    }
}
//---------------------Both-Shares-And-Wallet-Have-Input----------------------
function bothSharesAndWalletHaveInput(): boolean {
    return !isSharesInputEmpty() && !isWalletInputEmpty();
}
//-------------------------Change-Caret-To-Highlight--------------------------
function changeCaretToHighlight(): void {
    // TODO: Fix the fade functionality and make the caret src a global constant.
    currencyDropdownCaretImage.attr('src', '../static/images/down_caret_highlight.svg').fadeIn(200);
}
//---------------------------Change-Caret-To-Black----------------------------
function changeCaretToBlack(): void {
    // TODO: Fix the fade functionality and make the caret src a global constant.
    currencyDropdownCaretImage.attr('src', '../static/images/down_caret.svg').fadeIn(200);
}
//---------------------------Set-Selection-To-None----------------------------
function setSelectionToNone(): void {
    currencySelectionText.textContent = 'Select Currency';
    (selectedCurrencyImage as HTMLImageElement).src = defaultCurrencyImage;
}
//---------------------------Show-Add-Currency-Div----------------------------
function showAddCurrencyDiv(): void {
    addCurrencyDiv.style.display = 'block';
}
//---------------------------Hide-Add-Currency-Div----------------------------
function hideAddCurrencyDiv(): void {
    addCurrencyDiv.style.display = 'none';
}
//--------------------------Toggle-Currency-Dropdown--------------------------
function toggleCurrencyDropdown(): void {
    currencyDropdownSelection.classList.toggle('hide');
    currencyDropdownButton.classList.toggle('square-bottom');
}
//---------------------------Hide-Currency-Dropdown---------------------------
function hideCurrencyDropdown(): void {
    // TODO: Make this a jQuery faded toggle.
    currencyDropdownSelection.classList.add('hide');
    currencyDropdownButton.classList.remove('square-bottom');
}
//--------------------------------Clear-Inputs--------------------------------
function clearInputs(): void {
    (sharesInput as HTMLInputElement).value = '';
    (walletInput as HTMLInputElement).value = '';
    hidePopup();
    setSelectionToNone();
}
//-----------------------Initialize-Empty-Wallet-Shares-----------------------
function initializeEmptyWalletShares(valueInput: string | number): string | number {
    if (valueInput === '') {
        valueInput = 0;
    }
    return valueInput;
}
//---------------------------Is-Shares-Input-Empty----------------------------
function isSharesInputEmpty(): boolean {
    return (sharesInput as HTMLInputElement).value === '' || (sharesInput as HTMLInputElement).value === '0';
}
//---------------------------Is-Wallet-Input-Empty----------------------------
function isWalletInputEmpty(): boolean {
    return (walletInput as HTMLInputElement).value === '' || (walletInput as HTMLInputElement).value === '0';
}
//------------------------Shares-And-Wallet-Are-Empty-------------------------
function sharesAndWalletAreEmpty(): boolean {
    return isSharesInputEmpty() && isWalletInputEmpty();
}
//---------------------------------Show-Popup---------------------------------
function showPopup(message: string): void {
    popupText.textContent = message;
    popupDiv.style.display = 'flex';
}
//---------------------------------Hide-Popup---------------------------------
function hidePopup(): void {
    popupDiv.style.display = 'none';
}
//------------------------Shares-Or-Wallet-Have-Input-------------------------
// XOR check
function sharesOrWalletHaveInput(): boolean {
    if (isSharesInputEmpty() && isWalletInputEmpty()) {
        return false;
    } else if (!isSharesInputEmpty() && !isWalletInputEmpty()) {
        return false;
    } else {
        return true;
    }
}
//---------------------------Has-Selected-Currency----------------------------
function hasSelectedCurrency(): boolean {
    return currencySelectionText.textContent !== 'Select Currency';
}
//---------------------------Get-Selected-Currency----------------------------
function setSelectedCurrency(): void {
    let currencyChoiceText = this.getElementsByClassName('currency-option-text')[0].innerText;
    currencyChoiceText = sanitizeString(currencyChoiceText);
    selectedCurrencyText.textContent = currencyChoiceText;
    (selectedCurrencyImage as HTMLImageElement).src = getCurrencyLogoFromName(currencyChoiceText);
}
//=============================-Event-Listeners-==============================
let shouldLoadPage: boolean = loadPage(document.body, 'portfolio');
if (shouldLoadPage) {
    currencyOptionsArray.forEach((option: Element): void => {
        option.addEventListener('click', toggleCurrencyDropdown);
        option.addEventListener('click', setSelectedCurrency);
    });
    addCurrencyButton.addEventListener('click', showAddCurrencyDiv);
    cancelButton.addEventListener('click', hideAddCurrencyDiv);
    cancelButton.addEventListener('click', hideCurrencyDropdown);
    cancelButton.addEventListener('click', clearInputs);
    addButton.addEventListener('click', addItemSequence);
    currencyDropdownButton.addEventListener('click', toggleCurrencyDropdown);
    currencyDropdownCaret.on('mouseover', changeCaretToHighlight);
    currencyDropdownCaret.on('mouseout', changeCaretToBlack);
    inputArray.forEach((input: HTMLElement): void => {
        input.addEventListener('input', limitInput);
    });
    portfolioSelectorOption.on('click', selectPortfolioView);
}
//================================-Init-Load-=================================
if (shouldLoadPage) {
    showCorrectContainer();
}