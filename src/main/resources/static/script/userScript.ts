//=================================-Imports-==================================
import {hashPassword, loadPage} from "./globalScript.js";

//================================-Variables-=================================

//-----------------------------------Signup-----------------------------------
let signupContentContainer = document.getElementById('signup-form-container');
let signupTabSelector = document.getElementById('signup-tab-selector');
let signupUsernameInput = document.getElementById('signup-username-input');
let signupPasswordInput = document.getElementById('signup-password-input');
let signupConfirmPasswordInput = document.getElementById('signup-confirm-password-input');
let signupPasswordInputs = [signupPasswordInput, signupConfirmPasswordInput];
let termsAgreeCheckbox = document.getElementById('terms-agree-checkbox');
//-----------------------------------Login------------------------------------
let loginUsernameInput = document.getElementById('login-username-input');
let loginPasswordInput = document.getElementById('login-password-input');
let loginTabSelector = document.getElementById('login-tab-selector');
let loginContentContainer = document.getElementById('login-form-container');
//----------------------------------Buttons-----------------------------------
let signupButton = document.getElementById('signup-button-container');
let loginButton = document.getElementById('login-button-container');
//-----------------------------------Popup------------------------------------
let popupDivSignup = document.getElementById('signup-prompt-popup-div');
let popupTextSignup = document.getElementById('signup-prompt-popup-text');
let popupDivLogin = document.getElementById('login-prompt-popup-div');
let popupTextLogin = document.getElementById('login-prompt-popup-text');
//---------------------------------Selectors----------------------------------
let selectors = [signupTabSelector, loginTabSelector];
//-----------------------------------Cache------------------------------------
let currentUserInfo = "signup";
//=============================-Server-Functions-=============================

//---------------------------Send-Sign-Up-To-Server---------------------------
function sendSignUpToServer() {
    let hashedPassword = hashPassword((signupPasswordInput as HTMLInputElement).value);
    fetch('/user/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: (signupUsernameInput as HTMLInputElement).value,
            password: hashedPassword,
        })
    }).then(response => {
        if (response.status === 200) {
            window.location.href = '/portfolio/';
        } else {
            popupDivSignup.style.display = 'flex';
            popupTextSignup.textContent = 'Username already exists.';
        }
    })
}
//----------------------------Send-Login-To-Server----------------------------
function sendLoginToServer() {
    let hashedPassword = hashPassword((loginPasswordInput as HTMLInputElement).value);
    fetch('/user/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: (loginUsernameInput as HTMLInputElement).value,
            password: hashedPassword,
        })
    }).then(response => {
        if (response.status === 200) {
            window.location.href = '/portfolio/';
        } else {
            popupDivLogin.style.display = 'flex';
            popupTextLogin.textContent = 'Invalid username or password.';
        }
    })
}
//=============================-Client-Functions-=============================

//------------------------------Signup-Sequence-------------------------------
function signupSequence() {
    emptyFieldPopup();
    termsAgreedPopup();
    if (!hasEmptyFields() && passwordsMatch() && termsAgreed()) {
        sendSignUpToServer();
    }
}
//-------------------------------Login-Sequence-------------------------------
function loginSequence() {
    emptyFieldPopup();
    if (!hasEmptyFields()) {
        sendLoginToServer();
    }
}
//------------------------------Passwords-Match-------------------------------
function passwordsMatch() {
    let passwordInputValue = (signupPasswordInput as HTMLInputElement).value;
    let confirmPasswordInputValue = (signupConfirmPasswordInput as HTMLInputElement).value;
    return passwordInputValue === confirmPasswordInputValue;
}
//---------------------------Passwords-Match-Popup----------------------------
function passwordsMatchPopup() {
    if (!passwordsMatch()) {
        popupDivSignup.style.display = 'flex';
        popupTextSignup.textContent = 'Passwords do not match.';
    } else {
        popupDivSignup.style.display = 'none';
    }
}
//--------------------------------Terms-Agreed--------------------------------
function termsAgreed() {
    return (termsAgreeCheckbox as HTMLInputElement).checked;
}
//-----------------------------Terms-Agreed-Popup-----------------------------
function termsAgreedPopup() {
    console.log('termsAgreedPopup');
    console.log('test');
    if (!termsAgreed()) {
        popupDivSignup.style.display = 'flex';
        popupTextSignup.textContent = 'Please agree to the terms and conditions.';
    }
}
//------------------------------Has-Empty-Fields------------------------------
function hasEmptyFields() {
    if (currentUserInfo === 'signup') {
        return (signupUsernameInput as HTMLInputElement).value === '' ||
            (signupPasswordInput as HTMLInputElement).value === '' ||
            (signupConfirmPasswordInput as HTMLInputElement).value === '';
    } else {
        return (loginUsernameInput as HTMLInputElement).value === '' ||
            (loginPasswordInput as HTMLInputElement).value === '';
    }
}
//-----------------------------Empty-Field-Popup------------------------------
function emptyFieldPopup() {
    if (hasEmptyFields()) {
        if (currentUserInfo === 'signup') {
            popupDivSignup.style.display = 'flex';
            popupTextSignup.textContent = 'Please fill in all fields.';
        } else {
            popupDivLogin.style.display = 'flex';
            popupTextLogin.textContent = 'Please fill in all fields.';
        }
    }
}
//-------------------------Toggle-User-Info-Container-------------------------
function toggleUserInfoContainer() {
    let isCurrentlySignUp = signupContentContainer.style.display === 'flex';
    let isCurrentlyLogIn = loginContentContainer.style.display === 'flex';
    let clickedId = this.id;
    if (clickedId === 'signup-tab-selector' && !isCurrentlySignUp) {
        signupContentContainer.style.display = 'flex';
        loginContentContainer.style.display = 'none';
        currentUserInfo = 'signup';
    } else if (clickedId === 'login-tab-selector' && !isCurrentlyLogIn) {
        signupContentContainer.style.display = 'none';
        loginContentContainer.style.display = 'flex';
        currentUserInfo = 'login';
    }
}
//=============================-Event-Listeners-==============================
if (loadPage(document.body, 'user')) {
    selectors.forEach(selector => {
        selector.addEventListener('click', toggleUserInfoContainer)
    });
    signupButton.addEventListener('click', signupSequence);
    loginButton.addEventListener('click', loginSequence);
    signupPasswordInputs.forEach(signupPasswordInput => {
        signupPasswordInput.addEventListener('input', passwordsMatchPopup);
    });
}