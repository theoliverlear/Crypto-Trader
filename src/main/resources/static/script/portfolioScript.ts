//=================================-Imports-==================================
import {
    defaultCurrencyImage, formatDollars, getCodeByCurrencyName,
    getCurrencyLogoFromName, loadPage, sanitizeString
} from "./globalScript.js";
import {PortfolioAsset} from "./PortfolioAsset.js";
//================================-Variables-=================================

//-----------------------------------Inputs-----------------------------------
let sharesInput = document.getElementById('shares-input');
let walletInput = document.getElementById('wallet-input');
let inputs = [sharesInput, walletInput];
let inputArray = Array.from(inputs);
//---------------------------------Containers---------------------------------
const addCurrencyDiv = document.getElementById('add-currency-div');
const emptyPortfolioSection = document.getElementById('empty-portfolio-section');
let yourCurrenciesListSection = document.getElementById('your-currencies-list-section');
//-----------------------------Currency-Selection-----------------------------
const currencySelectionText = document.getElementById('currency-selection-text');
const currencySelectionOptions = document.getElementsByClassName('currency-dropdown-option');
const currencySelectionOptionsArray = Array.from(currencySelectionOptions);
let selectedCurrencyText = document.getElementById('selected-currency-text');
let selectedCurrencyImage = document.getElementById('your-currency-image');
//------------------------------Currency-Options------------------------------
let currencyOptionsDropdown = document.getElementsByClassName('currency-dropdown-option');
let currencyOptionsArray = Array.from(currencyOptionsDropdown);
const currencyDropdownSelection = document.getElementById('currency-dropdown-selection');
//-------------------------------Dropdown-Caret-------------------------------
const currencyDropdownCaret = document.getElementById('currency-dropdown-caret');
const currencyDropdownCaretImage = document.getElementById('currency-dropdown-caret-image');
//-----------------------------------Popup------------------------------------
let popupDiv = document.getElementById('popup-div');
let popupText = document.getElementById('popup-text');
//----------------------------------Buttons-----------------------------------
let addButton = document.getElementById('add-button');
const cancelButton = document.getElementById('cancel-button');
const addCurrencyButton = document.getElementById('add-currency-button');
const currencyDropdownButton = document.getElementById('currency-dropdown');
//=============================-Server-Functions-=============================

//-------------------------Get-Portfolio-From-Server--------------------------
// TODO: Either have a WebSocket updating their portfolio in real-time or have
//       a refresh button that updates the page.
async function getPortfolioFromServer() {
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
function sendPortfolioAssetToServer() {
    if (hasSelectedCurrency() && sharesOrWalletHaveInput()) {
        let currencyName = sanitizeString(selectedCurrencyText.textContent);
        let shares = initializeEmptyWalletShares((sharesInput as HTMLInputElement).value);
        let wallet = initializeEmptyWalletShares((walletInput as HTMLInputElement).value);
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
async function getIsEmptyPortfolioFromServer() {
    let response = await fetch('/portfolio/empty', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
    let responseJson = await response.text();
    return responseJson === 'true';
}
//=============================-Client-Functions-=============================

//----------------------------Load-Empty-Container----------------------------
function showCorrectContainer(): void {
    getIsEmptyPortfolioFromServer().then(isEmpty => {
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
function loadCurrencies() {
    yourCurrenciesListSection.innerHTML = '';
    getPortfolioFromServer().then(portfolio => {
        if (Array.isArray(portfolio.assets)) {
            portfolio.assets.forEach((asset: { currency: { name: any; currencyCode: any; }; shares: any; assetWalletDollars: string; totalValueInDollars: string; }) => {
                let currencyName = asset.currency.name;
                let currencyCode = asset.currency.currencyCode;
                let shares = asset.shares;
                let walletDollars = formatDollars(asset.assetWalletDollars);
                let totalAssetValue = formatDollars(asset.totalValueInDollars);
                addCurrencyToPage(currencyName, currencyCode, shares, walletDollars, totalAssetValue);
            })
        }
    });
}
//----------------------------Add-Currency-To-Page----------------------------
function addCurrencyToPage(currencyName: string, currencyCode: string, shares: number, walletDollars: string, totalAssetValue: string) {
    let currencyImageSrc = getCurrencyLogoFromName(currencyName);
    let currencyAsset = new PortfolioAsset(currencyName, currencyCode, shares, walletDollars, totalAssetValue, currencyImageSrc);
    let currencyDiv = document.createElement('div');
    currencyDiv.classList.add('simple-space-inline-div');
    currencyDiv.classList.add('your-currency-item');
    let currencyHtml = currencyAsset.buildHtml();
    currencyDiv.innerHTML = currencyHtml;
    yourCurrenciesListSection.appendChild(currencyDiv);
}
//-----------------------------Add-Item-Sequence------------------------------
async function addItemSequence() {
    if (sharesAndWalletAreEmpty()) {
        showPopup('Please fill out either the shares or wallet field.');
        return;
    }
    if (bothSharesAndWalletHaveInput()) {
        showPopup('Both shares and wallet have input. Please only input one.');
        return;
    }
    sendPortfolioAssetToServer();
    // let currencyName = sanitizeString(selectedCurrencyText.textContent);
    // let currencyCode = getCodeByCurrencyName(currencyName);
    // let shares = initializeEmptyWalletShares(sharesInput.value);
    // let walletDollars = initializeEmptyWalletShares(walletInput.value);
    // let assetValue = await initializeAssetValue(currencyCode, shares, walletDollars);
    // addCurrencyToPage(currencyName, currencyCode, shares, walletDollars, assetValue);
    clearInputs();
    hideCurrencyDropdown();
    hideAddCurrencyDiv();
    showCorrectContainer();
}
//---------------------------Initialize-Asset-Value---------------------------
async function initializeAssetValue(currencyCode: any, shares: number, walletDollars: any) {
    if (shares === 0) {
        return walletDollars;
    } else {
        let response = await fetch('/portfolio/currency/value', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                currencyCode: currencyCode,
                shares: shares,
            })
        }).then(response => {
            return response.json();
        });
        return formatDollars(response.value);
    }
}
//--------------------------------Limit-Input---------------------------------
function limitInput() {
    if (this.value >= 10_000_000) {
        this.value = 10_000_000;
    }
}
//---------------------Both-Shares-And-Wallet-Have-Input----------------------
function bothSharesAndWalletHaveInput() {
    return hasSharesInput() && hasWalletInput();
}
//-------------------------Change-Caret-To-Highlight--------------------------
function changeCaretToHighlight() {
    (currencyDropdownCaretImage as HTMLImageElement).src = '/images/down_caret_highlight.svg';
}
//---------------------------Change-Caret-To-Black----------------------------
function changeCaretToBlack() {
    (currencyDropdownCaretImage as HTMLImageElement).src = '/images/down_caret.svg';
}
//---------------------------Set-Selection-To-None----------------------------
function setSelectionToNone() {
    currencySelectionText.textContent = 'Select Currency';
    (selectedCurrencyImage as HTMLImageElement).src = defaultCurrencyImage;
}
//---------------------------Show-Add-Currency-Div----------------------------
function showAddCurrencyDiv() {
    addCurrencyDiv.style.display = 'block';
}
//---------------------------Hide-Add-Currency-Div----------------------------
function hideAddCurrencyDiv() {
    addCurrencyDiv.style.display = 'none';
}
//--------------------------Toggle-Currency-Dropdown--------------------------
function toggleCurrencyDropdown() {
    currencyDropdownSelection.classList.toggle('hide');
    currencyDropdownButton.classList.toggle('square-bottom');
}
//---------------------------Hide-Currency-Dropdown---------------------------
function hideCurrencyDropdown() {
    currencyDropdownSelection.classList.add('hide');
    currencyDropdownButton.classList.remove('square-bottom');
}
//--------------------------------Clear-Inputs--------------------------------
function clearInputs() {
    (sharesInput as HTMLInputElement).value = '';
    (walletInput as HTMLInputElement).value = '';
    hidePopup();
    setSelectionToNone();
}
//-----------------------Initialize-Empty-Wallet-Shares-----------------------
function initializeEmptyWalletShares(valueInput: string | number) {
    if (valueInput === '') {
        valueInput = 0;
    }
    return valueInput;
}
//------------------------Shares-And-Wallet-Are-Empty-------------------------
function sharesAndWalletAreEmpty() {
    return (sharesInput as HTMLInputElement).value === '' && (walletInput as HTMLInputElement).value === '';
}
//---------------------------------Show-Popup---------------------------------
function showPopup(message: string) {
    popupText.textContent = message;
    popupDiv.style.display = 'flex';
}
//---------------------------------Hide-Popup---------------------------------
function hidePopup() {
    popupDiv.style.display = 'none';
}
//------------------------Shares-Or-Wallet-Have-Input-------------------------
// XOR check
function sharesOrWalletHaveInput() {
    if (hasSharesInput() && hasWalletInput()) {
        return false;
    } else if (!hasSharesInput() && !hasWalletInput()) {
        return false;
    } else {
        return true;
    }
}
//---------------------------Has-Selected-Currency----------------------------
function hasSelectedCurrency() {
    return currencySelectionText.textContent !== 'Select Currency';
}
//------------------------------Has-Shares-Input------------------------------
function hasSharesInput() {
    return (sharesInput as HTMLInputElement).value !== '' && (sharesInput as HTMLInputElement).value !== '0';
}
//------------------------------Has-Wallet-Input------------------------------
function hasWalletInput() {
    return (walletInput as HTMLInputElement).value !== '' && (walletInput as HTMLInputElement).value !== '0';
}
//---------------------------Get-Selected-Currency----------------------------
function setSelectedCurrency() {
    let currencyChoiceText = this.getElementsByClassName('currency-option-text')[0].innerText;
    currencyChoiceText = sanitizeString(currencyChoiceText);
    selectedCurrencyText.textContent = currencyChoiceText;
    (selectedCurrencyImage as HTMLImageElement).src = getCurrencyLogoFromName(currencyChoiceText);
}
//=============================-Event-Listeners-==============================
let shouldLoadPage: boolean = loadPage(document.body, 'portfolio');
if (shouldLoadPage) {
    currencyOptionsArray.forEach(option => {
        option.addEventListener('click', toggleCurrencyDropdown);
        option.addEventListener('click', setSelectedCurrency);
    });
    addCurrencyButton.addEventListener('click', showAddCurrencyDiv);
    cancelButton.addEventListener('click', hideAddCurrencyDiv);
    cancelButton.addEventListener('click', hideCurrencyDropdown);
    cancelButton.addEventListener('click', clearInputs);
    addButton.addEventListener('click', addItemSequence);
    currencyDropdownButton.addEventListener('click', toggleCurrencyDropdown);
    currencyDropdownCaret.addEventListener('mouseover', changeCaretToHighlight);
    currencyDropdownCaret.addEventListener('mouseout', changeCaretToBlack);
    inputArray.forEach(input => {
        input.addEventListener('input', limitInput);
    });
}
//================================-Init-Load-=================================
if (shouldLoadPage) {
    showCorrectContainer();
}