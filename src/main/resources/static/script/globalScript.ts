import * as CryptoJS from 'crypto-js';
const defaultCurrencyImage = '/images/default_currency_icon.png';
let isLoggedIn = false;
let logoutButton = document.getElementById('logout-button-div');
let accountImageContainer = document.getElementById('account-image-container');
//=============================-Server-Functions-=============================

//---------------------------Send-Logout-To-Server----------------------------
function sendLogoutToServer(): void {
    fetch('/user/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (response.ok) {
            window.location.href = '/';
        }
    }).catch(error => {
        console.error('Error: ', error);
    });
}
//-----------------------------Set-Is-Logged-In-------------------------------
function setIsLoggedIn() {
    getIsLoggedInFromServer().then(response => {
        isLoggedIn = response;
    });
}
//------------------------Get-Is-Logged-In-From-Server------------------------
async function getIsLoggedInFromServer() {
    let response = await fetch('/user/loggedin', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).catch(error => {
        console.error('Error: ', error);
    });
    return true; // TODO: Change to response.ok
}
//=============================-Client-Functions-=============================

//------------------------------Logout-Sequence-------------------------------
function logoutSequence() {
    sendLogoutToServer();
    hideLogoutButton();
}
//-----------------------------Show-Logout-Button-----------------------------
function showLogoutButton() {
    if (isLoggedIn) {
        logoutButton.style.display = 'flex';
    }
}
//-----------------------------Hide-Logout-Button-----------------------------
function hideLogoutButton() {
    logoutButton.style.display = 'none';
}
//------------------------Password-Contains-Artifacts-------------------------
function passwordContainsArtifacts(password: string) {
    if (password.length === 0 || password.includes(' ')) {
        return true;
    }
    return false;
}
//-------------------------------Format-Dollars-------------------------------
function formatDollars(assetPrice: string) {
    let assetPriceNumber: number = parseFloat(assetPrice);
    let formatter = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' });
    return formatter.format(assetPriceNumber);
}
//-------------------------Get-Code-By-Currency-Name--------------------------
function getCodeByCurrencyName(currencyName: string): string {
    let code = '';
    switch (currencyName) {
        case 'Bitcoin':
            code = 'BTC';
            break;
        case 'Ethereum':
            code = 'ETH';
            break;
        case 'Litecoin':
            code = 'LTC';
            break;
        default:
            code = 'UNKNOWN';
            break;
    }
    return code;
}
//-------------------------------Hash-Password--------------------------------
function hashPassword(password: string) {
    let hashedPassword = CryptoJS.SHA256(password);
    return hashedPassword.toString();
}
//------------------------Get-Currency-Logo-From-Name-------------------------
function getCurrencyLogoFromName(currencyName: string) {
    let currencyLogoSrc = '';
    switch (currencyName) {
        case 'Bitcoin':
            currencyLogoSrc = '/images/logo/currency/bitcoin_logo.png';
            break;
        case 'Ethereum':
            currencyLogoSrc = '/images/logo/currency/ethereum_logo.png';
            break;
        case 'Litecoin':
            currencyLogoSrc = '/images/logo/currency/litecoin_logo.png';
            break;
        default:
            currencyLogoSrc = defaultCurrencyImage;
            break;
    }
    return currencyLogoSrc;
}
//------------------------------Sanitize-String-------------------------------
function sanitizeString(input: string) {
    return input.trim().replace("\n", "").replace("\r", "");
}
//-------------------------------Load-Page------------------------------------
function loadPage(bodyElement: HTMLElement, page: string): boolean {
    return bodyElement.getAttribute('data-page') === page;
}
//=============================-Event-Listeners-==============================
logoutButton.addEventListener('click', logoutSequence);
accountImageContainer.addEventListener('mouseover', showLogoutButton);
//================================-Init-Load-=================================
setIsLoggedIn();
//=================================-Exports-==================================
export {hashPassword, getCurrencyLogoFromName, passwordContainsArtifacts,
    sanitizeString, formatDollars, getCodeByCurrencyName, logoutSequence,
    loadPage};
export {defaultCurrencyImage, isLoggedIn};