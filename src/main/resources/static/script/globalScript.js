const defaultCurrencyImage = '/images/default_currency_icon.png';
//=============================-Client-Functions-=============================

//------------------------Password-Contains-Artifacts-------------------------
function passwordContainsArtifacts(password) {
    if (password.length === 0 || password.includes(' ')) {
        return true;
    }
    return false;
}
function formatDollars(assetPrice) {
    return assetPrice.toFixed(2);
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
//=================================-Exports-==================================
export {hashPassword, getCurrencyLogoFromName, passwordContainsArtifacts, sanitizeString, formatDollars};
export {defaultCurrencyImage};