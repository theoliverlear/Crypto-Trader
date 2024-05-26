//=================================-Imports-==================================
import {hashPassword, loadPage} from "./globalScript.js";
//================================-Variables-=================================

//-----------------------------------Signup-----------------------------------
let signupContentContainer: HTMLElement = document.getElementById('signup-form-container');
let signupTabSelector: HTMLElement = document.getElementById('signup-tab-selector');
let signupUsernameInput: HTMLElement = document.getElementById('signup-username-input');
let signupPasswordInput: HTMLElement = document.getElementById('signup-password-input');
let signupConfirmPasswordInput: HTMLElement = document.getElementById('signup-confirm-password-input');
let signupPasswordInputs: HTMLElement[] = [signupPasswordInput, signupConfirmPasswordInput];
let termsAgreeCheckbox: HTMLElement = document.getElementById('terms-agree-checkbox');
//-----------------------------------Login------------------------------------
let loginUsernameInput: HTMLElement = document.getElementById('login-username-input');
let loginPasswordInput: HTMLElement = document.getElementById('login-password-input');
let loginTabSelector: HTMLElement = document.getElementById('login-tab-selector');
let loginContentContainer: HTMLElement = document.getElementById('login-form-container');
//----------------------------------Buttons-----------------------------------
let signupButton: HTMLElement = document.getElementById('signup-button-container');
let loginButton: HTMLElement = document.getElementById('login-button-container');
//-----------------------------------Popup------------------------------------
let popupDivSignup: HTMLElement = document.getElementById('signup-prompt-popup-div');
let popupTextSignup: HTMLElement = document.getElementById('signup-prompt-popup-text');
let popupDivLogin: HTMLElement = document.getElementById('login-prompt-popup-div');
let popupTextLogin: HTMLElement = document.getElementById('login-prompt-popup-text');
//---------------------------------Selectors----------------------------------
let selectors: HTMLElement[] = [signupTabSelector, loginTabSelector];
//-----------------------------------Cache------------------------------------
let currentUserInfo: string = "signup"; // TODO: Refactor to enum. That is more readable.
//=============================-Server-Functions-=============================

//---------------------------Send-Sign-Up-To-Server---------------------------
function sendSignUpToServer(): void {
    let hashedPassword: string = hashPassword((signupPasswordInput as HTMLInputElement).value);
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
function sendLoginToServer(): void {
    let hashedPassword: string = hashPassword((loginPasswordInput as HTMLInputElement).value);
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
function signupSequence(): void {
    emptyFieldPopup();
    termsAgreedPopup();
    if (!hasEmptyFields() && passwordsMatch() && termsAgreed()) {
        sendSignUpToServer();
    }
}
//-------------------------------Login-Sequence-------------------------------
function loginSequence(): void {
    emptyFieldPopup();
    if (!hasEmptyFields()) {
        sendLoginToServer();
    }
}
//------------------------------Passwords-Match-------------------------------
function passwordsMatch(): boolean {
    let passwordInputValue: string = (signupPasswordInput as HTMLInputElement).value;
    let confirmPasswordInputValue: string = (signupConfirmPasswordInput as HTMLInputElement).value;
    return passwordInputValue === confirmPasswordInputValue;
}
//---------------------------Passwords-Match-Popup----------------------------
function passwordsMatchPopup(): void {
    if (!passwordsMatch()) {
        popupDivSignup.style.display = 'flex';
        popupTextSignup.textContent = 'Passwords do not match.';
    } else {
        popupDivSignup.style.display = 'none';
    }
}
//--------------------------------Terms-Agreed--------------------------------
function termsAgreed(): boolean {
    return (termsAgreeCheckbox as HTMLInputElement).checked;
}
//-----------------------------Terms-Agreed-Popup-----------------------------
function termsAgreedPopup(): void {
    if (!termsAgreed()) {
        popupDivSignup.style.display = 'flex';
        popupTextSignup.textContent = 'Please agree to the terms and conditions.';
    }
}
//------------------------------Has-Empty-Fields------------------------------
function hasEmptyFields(): boolean {
    // TODO: Refactor so empty input checks are functions.
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
function emptyFieldPopup(): void {
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
function toggleUserInfoContainer(): void {
    let isCurrentlySignUp: boolean = signupContentContainer.style.display === 'flex';
    let isCurrentlyLogIn: boolean = loginContentContainer.style.display === 'flex';
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
    selectors.forEach((selector: HTMLElement): void => {
        selector.addEventListener('click', toggleUserInfoContainer)
    });
    signupButton.addEventListener('click', signupSequence);
    loginButton.addEventListener('click', loginSequence);
    signupPasswordInputs.forEach((signupPasswordInput: HTMLElement): void => {
        signupPasswordInput.addEventListener('input', passwordsMatchPopup);
    });
}