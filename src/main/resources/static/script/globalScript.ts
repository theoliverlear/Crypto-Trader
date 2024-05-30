//=================================-Imports-==================================
import * as CryptoJS from 'crypto-js';
//================================-Variables-=================================
const defaultCurrencyImage = '/images/default_currency_icon.png';
let isLoggedIn: boolean = false;
let logoutButton: HTMLElement = document.getElementById('logout-button-div');
let accountImageContainer: HTMLElement = document.getElementById('account-image-container');
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
function setIsLoggedIn(): void {
    getIsLoggedInFromServer().then(response => {
        isLoggedIn = response;
    });
}
//------------------------Get-Is-Logged-In-From-Server------------------------
async function getIsLoggedInFromServer(): Promise<boolean> {
    let response = await fetch('/user/loggedin', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).catch(error => {
        console.error('Error: ', error);
    });
    if (response) {
        return response.ok;
    } else {
        return false;
    }
}
//=============================-Client-Functions-=============================

//------------------------------Logout-Sequence-------------------------------
function logoutSequence(): void {
    sendLogoutToServer();
    hideLogoutButton();
}
//-----------------------------Show-Logout-Button-----------------------------
function showLogoutButton(): void {
    if (isLoggedIn) {
        logoutButton.style.display = 'flex';
    }
}
//-----------------------------Hide-Logout-Button-----------------------------
function hideLogoutButton(): void {
    logoutButton.style.display = 'none';
}
//------------------------Password-Contains-Artifacts-------------------------
function passwordContainsArtifacts(password: string): boolean {
    if (password.length === 0 || password.includes(' ')) {
        return true;
    }
    return false;
}
//-------------------------------Format-Dollars-------------------------------
function formatDollars(assetPrice: string, decimalPlaces: number = 2): string {
    let assetPriceNumber: number = parseFloat(assetPrice);
    let formatter: Intl.NumberFormat = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
        minimumFractionDigits: decimalPlaces,
        maximumFractionDigits: decimalPlaces
    });
    return formatter.format(assetPriceNumber);
}
//-------------------------Get-Code-By-Currency-Name--------------------------
function getCodeByCurrencyName(currencyName: string): string {
    let code: string = '';
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
function getNameByCurrencyCode(currencyCode: string): string {
    let currencyName: string = '';
    switch (currencyCode) {
        case 'BTC':
            currencyName = 'Bitcoin';
            break;
        case 'ETH':
            currencyName = 'Ethereum';
            break;
        case 'LTC':
            currencyName = 'Litecoin';
            break;
        default:
            currencyName = 'UNKNOWN';
            break;
    }
    return currencyName;
}
//-------------------------------Hash-Password--------------------------------
function hashPassword(password: string): string {
    let hashedPassword = CryptoJS.SHA256(password);
    return hashedPassword.toString();
}
//------------------------Get-Currency-Logo-From-Name-------------------------
function getCurrencyLogoFromName(currencyName: string): string {
    let currencyLogoSrc: string = '';
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
function formatDate(dateString: string): string {
    let date = new Date(dateString);
    let formattedDate = date.toLocaleDateString();
    let formattedTime = date.toLocaleTimeString();
    return `${formattedDate}, ${formattedTime}`;
}
//------------------------------Sanitize-String-------------------------------
function sanitizeString(input: string): string {
    return input.trim().replace("\n", "").replace("\r", "");
}
//-------------------------------Load-Page------------------------------------
function loadPage(bodyElement: HTMLElement, pageName: string): boolean {
    return bodyElement.getAttribute('data-page') === pageName;
}
//=============================-Event-Listeners-==============================
logoutButton.addEventListener('click', logoutSequence);
accountImageContainer.addEventListener('mouseover', showLogoutButton);
//================================-Init-Load-=================================
setIsLoggedIn();
//=================================-Exports-==================================
export {hashPassword, getCurrencyLogoFromName, passwordContainsArtifacts,
    sanitizeString, formatDollars, getCodeByCurrencyName, logoutSequence,
    loadPage, formatDate, getNameByCurrencyCode};
export {defaultCurrencyImage, isLoggedIn};