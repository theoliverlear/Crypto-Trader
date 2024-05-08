
console.log('portfolioScript.js loaded');
const currencyDropdownCaret = document.getElementById('currency-dropdown-caret');
const currencyDropdownCaretImage = document.getElementById('currency-dropdown-caret-image');

function changeCaretToHighlight() {
    currencyDropdownCaretImage.src = '../static/images/down_caret_highlight.svg';
}
function changeCaretToBlack() {
    currencyDropdownCaretImage.src = '../static/images/down_caret.svg';
}
currencyDropdownCaret.addEventListener('mouseover', changeCaretToHighlight);
currencyDropdownCaret.addEventListener('mouseout', changeCaretToBlack);
const currencyDropdownButton = document.getElementById('currency-dropdown');
const currencyDropdownSelection = document.getElementById('currency-dropdown-selection');
function toggleCurrencyDropdown() {
    currencyDropdownSelection.classList.toggle('hide');
    currencyDropdownButton.classList.toggle('square-bottom');
}
function hideCurrencyDropdown() {
    currencyDropdownSelection.classList.add('hide');
    currencyDropdownButton.classList.remove('square-bottom');
}
let selectedCurrencyImage = document.getElementById('your-currency-image');
const defaultCurrencyImage = '../static/images/default_currency_icon.png';
function setSelectionToNone() {
    currencySelectionText.textContent = 'Select Currency';
    selectedCurrencyImage.src = defaultCurrencyImage;
}
let addButton = document.getElementById('add-button');
function addItemSequence() {
    if (emptyInfo()) {
        showPopup();
        return;
    }
    sendPortfolioAssetToServer();
    clearInputs();
    hideCurrencyDropdown();
    hideAddCurrencyDiv();
    updatePage();
}
addButton.addEventListener('click', addItemSequence);
currencyDropdownButton.addEventListener('click', toggleCurrencyDropdown);
const currencySelectionText = document.getElementById('currency-selection-text');
const currencySelectionOptions = document.getElementsByClassName('currency-dropdown-option');
const currencySelectionOptionsArray = Array.from(currencySelectionOptions);

const addCurrencyButton = document.getElementById('add-currency-button');
const addCurrencyDiv = document.getElementById('add-currency-div');
const cancelButton = document.getElementById('cancel-button');

function showAddCurrencyDiv() {
    addCurrencyDiv.style.display = 'block';
}
function hideAddCurrencyDiv() {
    addCurrencyDiv.style.display = 'none';
}

addCurrencyButton.addEventListener('click', showAddCurrencyDiv);
cancelButton.addEventListener('click', hideAddCurrencyDiv);
cancelButton.addEventListener('click', hideCurrencyDropdown);

let sharesInput = document.getElementById('shares-input');
let walletInput = document.getElementById('wallet-input');

function sendPortfolioAssetToServer() {
    if (hasSelectedCurrency() && sharesOrWalletHasInput()) {
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
function clearInputs() {
    sharesInput.value = '';
    walletInput.value = '';
    setSelectionToNone();
}
function initializeEmptyWalletShares(valueInput) {
    if (valueInput === '') {
        valueInput = 0;
    }
    return valueInput;
}
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
function updatePage() {
    
}
function sharesAndWalletAreEmpty() {
    return sharesInput.value === '' && walletInput.value === '';
}
let popupDiv = document.getElementById('popup-div');
function showPopup() {
    popupDiv.style.display = 'flex';
}
function hidePopup() {
    popupDiv.style.display = 'none';
}
// XOR check
function sharesOrWalletHasInput() {
    if (hasSharesInput() && hasWalletInput()) {
        return false;
    } else if (!hasSharesInput() && !hasWalletInput()) {
        return false;
    } else {
        return true;
    }
}
function hasSelectedCurrency() {
    return currencySelectionText.textContent !== 'Select Currency';
}
function hasSharesInput() {
    return sharesInput.value !== '';
}
function hasWalletInput() {
    return walletInput.value !== '';
}
let currencyOptionsDropdown = document.getElementsByClassName('currency-dropdown-option');
let currencyOptionsArray = Array.from(currencyOptionsDropdown);

currencyOptionsArray.forEach(option => {
    option.addEventListener('click', toggleCurrencyDropdown);
    option.addEventListener('click', setSelectedCurrency);
});
let selectedCurrencyText = document.getElementById('selected-currency-text');
function setSelectedCurrency() {
    let currencyChoiceText = this.getElementsByClassName('currency-option-text')[0].innerText;
    currencyChoiceText = sanitizeString(currencyChoiceText);
    selectedCurrencyText.textContent = currencyChoiceText;
    selectedCurrencyImage.src = getCurrencyLogoFromName(currencyChoiceText);
}
function sanitizeString(input) {
    return input.trim().replace("\n", "").replace("\r", "");
}
function getCurrencyLogoFromName(currencyName) {
    let currencyLogoSrc = '';
    switch (currencyName) {
        case 'Bitcoin':
            currencyLogoSrc = '../static/images/logo/currency/bitcoin_logo.png';
            break;
        case 'Ethereum':
            currencyLogoSrc = '../static/images/logo/currency/ethereum_logo.png';
            break;
        case 'Litecoin':
            currencyLogoSrc = '../static/images/logo/currency/litecoin_logo.png';
            break;
        default:
            currencyLogoSrc = defaultCurrencyImage;
            break;
    }
    return currencyLogoSrc;
}