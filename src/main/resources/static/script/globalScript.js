const CryptoJS = require('crypto-js');
function passwordContainsArtifacts(password) {
    // 0 long or has spaces
    if (password.length === 0 || password.includes(' ')) {
        return true;
    }
    return false;
}
function hashPassword(password) {
    let hashedPassword = CryptoJS.SHA256(password);
    return hashedPassword.toString();
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
    }
    return currencyLogoSrc;
}
export {hashPassword, getCurrencyLogoFromName, passwordContainsArtifacts};
// module.exports = {hashPassword, getCurrencyLogoFromName, passwordContainsArtifacts};