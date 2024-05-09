//=================================-Imports-==================================
import {
    defaultCurrencyImage,
    getCurrencyLogoFromName,
    sanitizeString
} from "./globalScript";
//================================-Variables-=================================

//-----------------------------------Inputs-----------------------------------
let sharesInput = document.getElementById('shares-input');
let walletInput = document.getElementById('wallet-input');
//---------------------------------Containers---------------------------------
const addCurrencyDiv = document.getElementById('add-currency-div');
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
//=============================-Client-Functions-=============================

//-----------------------------Add-Item-Sequence------------------------------
function addItemSequence() {
    if (sharesAndWalletAreEmpty()) {
        showPopup();
        return;
    }
    sendPortfolioAssetToServer();
    clearInputs();
    hideCurrencyDropdown();
    hideAddCurrencyDiv();
    updatePage();
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
    setSelectionToNone();
}
//-----------------------Initialize-Empty-Wallet-Shares-----------------------
function initializeEmptyWalletShares(valueInput) {
    if (valueInput === '') {
        valueInput = 0;
    }
    return valueInput;
}
//--------------------------------Update-Page---------------------------------
function updatePage() {
    
}
//------------------------Shares-And-Wallet-Are-Empty-------------------------
function sharesAndWalletAreEmpty() {
    return sharesInput.value === '' && walletInput.value === '';
}
//---------------------------------Show-Popup---------------------------------
function showPopup() {
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
    return sharesInput.value !== '';
}
//------------------------------Has-Wallet-Input------------------------------
function hasWalletInput() {
    return walletInput.value !== '';
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