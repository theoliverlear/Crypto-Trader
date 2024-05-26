//=================================-Imports-==================================
import {loadPage, logoutSequence} from "./globalScript";

//================================-Variables-=================================

//----------------------------------Buttons-----------------------------------
let accountLogoutButton = document.getElementById('logout-button-div');
//=============================-Event-Listeners-==============================
if (loadPage(document.body, 'account')) {
    accountLogoutButton.addEventListener('click', logoutSequence);
}
