let CryptoJS = require("crypto-js");
function passwordContainsArtifacts(password) {
    // 0 long or has spaces
}
function hashPassword(password) {
    let hashedPassword = CryptoJS.SHA256(password);
    return hashedPassword.toString();
}
module.exports = {hashPassword};