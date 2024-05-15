import {logoutSequence} from "./globalScript.js";
let accountLogoutButton = document.getElementById('account-logout-button');
accountLogoutButton.addEventListener('click', logoutSequence);