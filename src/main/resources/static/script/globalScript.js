const defaultCurrencyImage = '/images/default_currency_icon.png';
let isLoggedIn = true; // Temporarily set to true while testing.
let logoutButton = document.getElementById('logout-button-div');
//=============================-Server-Functions-=============================

//---------------------------Send-Logout-To-Server----------------------------
function sendLogoutToServer() {
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
    return response.ok;
}
//=============================-Client-Functions-=============================

//------------------------------Logout-Sequence-------------------------------
function logoutSequence() {
    sendLogoutToServer();
    hideLogoutButton();
}
//-----------------------------Hide-Logout-Button-----------------------------
function hideLogoutButton() {
    logoutButton.style.display = 'none';
}
//------------------------Password-Contains-Artifacts-------------------------
function passwordContainsArtifacts(password) {
    if (password.length === 0 || password.includes(' ')) {
        return true;
    }
    return false;
}
//-------------------------------Format-Dollars-------------------------------
function formatDollars(assetPrice) {
    return assetPrice.toFixed(2);
}
//-------------------------Get-Code-By-Currency-Name--------------------------
function getCodeByCurrencyName(currencyName) {
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
function hashPassword(password) {
    let hashedPassword = CryptoJS.SHA256(password);
    return hashedPassword.toString();
}
//------------------------Get-Currency-Logo-From-Name-------------------------
function getCurrencyLogoFromName(currencyName) {
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
function sanitizeString(input) {
    return input.trim().replace("\n", "").replace("\r", "");
}
//=============================-Event-Listeners-==============================
logoutButton.addEventListener('click', logoutSequence);
//================================-Init-Load-=================================
setIsLoggedIn();
//=================================-Exports-==================================
export {hashPassword, getCurrencyLogoFromName, passwordContainsArtifacts,
        sanitizeString, formatDollars, getCodeByCurrencyName};
export {defaultCurrencyImage, isLoggedIn};