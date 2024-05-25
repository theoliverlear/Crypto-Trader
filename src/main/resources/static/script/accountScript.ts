import {loadPage, logoutSequence} from "./globalScript";
let accountLogoutButton = document.getElementById('logout-button-div');
if (loadPage(document.body, 'account')) {
    accountLogoutButton.addEventListener('click', logoutSequence);
}
