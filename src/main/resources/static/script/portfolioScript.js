//=================================-Imports-==================================
import {
    defaultCurrencyImage, formatDollars,
    getCurrencyLogoFromName,
    sanitizeString
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
        let shares = initializeEmptyWalletShares(sharesInput.value);
        let wallet = initializeEmptyWalletShares(walletInput.value);

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
function showCorrectContainer() {
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
    getPortfolioFromServer().then(portfolio => {
        console.log(portfolio);
        if (Array.isArray(portfolio.assets)) {
            portfolio.assets.forEach(asset => {
                let currencyName = asset.currency.name;
                let currencyCode = asset.currency.currencyCode;
                let shares = asset.shares;
                let walletDollars = formatDollars(asset.assetWalletDollars);
                let totalAssetValue = formatDollars(asset.totalValueInDollars);
                addCurrencyToPage(currencyName, currencyCode, shares, walletDollars, totalAssetValue);
                console.log(asset);
            });
        } else {
            console.log('No assets in portfolio.');
        }
    });
}
//----------------------------Add-Currency-To-Page----------------------------
function addCurrencyToPage(currencyName, currencyCode, shares, walletDollars, totalAssetValue) {
    console.log(currencyName, currencyCode, shares, walletDollars, totalAssetValue);
    let currencyImageSrc = getCurrencyLogoFromName(currencyName);
    let currencyAsset = new PortfolioAsset(currencyName, currencyCode, shares, walletDollars, totalAssetValue, currencyImageSrc);
    let currencyDiv = document.createElement('div');
    currencyDiv.classList.add('simple-space-inline-div');
    currencyDiv.classList.add('your-currency-item');
    let currencyHtml = currencyAsset.buildHtml();
    console.log(currencyHtml);
    currencyDiv.innerHTML = currencyHtml;
    yourCurrenciesListSection.appendChild(currencyDiv);
}
//-----------------------------Add-Item-Sequence------------------------------
function addItemSequence() {
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
    loadCurrencies();
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
    currencyDropdownCaretImage.src = '../static/images/down_caret_highlight.svg';
}
//---------------------------Change-Caret-To-Black----------------------------
function changeCaretToBlack() {
    currencyDropdownCaretImage.src = '../static/images/down_caret.svg';
}
//---------------------------Set-Selection-To-None----------------------------
function setSelectionToNone() {
    currencySelectionText.textContent = 'Select Currency';
    selectedCurrencyImage.src = defaultCurrencyImage;
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
    sharesInput.value = '';
    walletInput.value = '';
    hidePopup();
    setSelectionToNone();
}
//-----------------------Initialize-Empty-Wallet-Shares-----------------------
function initializeEmptyWalletShares(valueInput) {
    if (valueInput === '') {
        valueInput = 0;
    }
    return valueInput;
}
//------------------------Shares-And-Wallet-Are-Empty-------------------------
function sharesAndWalletAreEmpty() {
    return sharesInput.value === '' && walletInput.value === '';
}
//---------------------------------Show-Popup---------------------------------
function showPopup(message) {
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
    return sharesInput.value !== '' && sharesInput.value !== '0';
}
//------------------------------Has-Wallet-Input------------------------------
function hasWalletInput() {
    return walletInput.value !== '' && walletInput.value !== '0';
}
//---------------------------Get-Selected-Currency----------------------------
function setSelectedCurrency() {
    let currencyChoiceText = this.getElementsByClassName('currency-option-text')[0].innerText;
    currencyChoiceText = sanitizeString(currencyChoiceText);
    selectedCurrencyText.textContent = currencyChoiceText;
    selectedCurrencyImage.src = getCurrencyLogoFromName(currencyChoiceText);
}
//=============================-Event-Listeners-==============================
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
//================================-Init-Load-=================================
showCorrectContainer();