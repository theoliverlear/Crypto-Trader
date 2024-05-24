//=================================-Imports-==================================
import {hashPassword} from "./globalScript.js";
//================================-Variables-=================================

//-----------------------------------Inputs-----------------------------------
let userUsernameInput = document.getElementById('signup-username-input');
let userPasswordInput = document.getElementById('signup-password-input');
let userPasswordConfirmInput = document.getElementById('signup-confirm-password-input');
let signupTermsCheckbox = document.getElementById('signup-terms-agree-checkbox');
//--------------------------------Input-Values--------------------------------
let userUsernameInputValue = (userUsernameInput as HTMLInputElement).value;
let userPasswordInputValue = (userPasswordInput as HTMLInputElement).value;
let userPasswordConfirmInputValue = (userPasswordConfirmInput as HTMLInputElement).value;
let signupTermsCheckboxValue = (signupTermsCheckbox as HTMLInputElement).checked;
//-----------------------------------Popup------------------------------------
let popupDiv = document.getElementById('signup-prompt-popup-div');
let popupText = document.getElementById('signup-prompt-popup-text');
let signupButton = document.getElementById('signup-button-container');
//=============================-Server-Functions-=============================

//-------------------------Send-Signup-Data-To-Server-------------------------
function sendSignupDataToServer() {
    let hashedPassword = hashPassword(userPasswordInputValue);
    console.log(hashedPassword);
    fetch('/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: userUsernameInputValue,
            password: userPasswordInputValue
        })
    }).then(response => {
        if (response.status === 200) {
            window.location.href = '/portfolio/';
        } else {
            popupDiv.style.display = 'block';
            popupText.textContent = 'Username already exists.';
        }
    });
}
//=============================-Client-Functions-=============================

//----------------------------Password-Match-Popup----------------------------
function passwordMatchPopup(): void {
    if (!passwordsMatch()) {
        console.log('Passwords do not match.');
        popupDiv.style.display = 'block';
        popupText.textContent = 'Passwords do not match.';
    } else {
        popupText.textContent = '';
        popupDiv.style.display = 'none';
    }
}
//-----------------------------Signup-Terms-Popup-----------------------------
function signupTermsPopup() {
    if (!termsAgreed()) {
        popupDiv.style.display = 'block';
        popupText.textContent = 'Please agree to the terms and conditions.';
    } else {
        popupText.textContent = '';
        popupDiv.style.display = 'none';
    }
}
//------------------------------Passwords-Match-------------------------------
function passwordsMatch() {
    userPasswordInputValue = (userPasswordInput as HTMLInputElement).value;
    userPasswordConfirmInputValue = (userPasswordConfirmInput as HTMLInputElement).value;
    return userPasswordInputValue === userPasswordConfirmInputValue;
}
//--------------------------------Terms-Agreed--------------------------------
function termsAgreed() {
    signupTermsCheckboxValue = (signupTermsCheckbox as HTMLInputElement).checked;
    return signupTermsCheckboxValue;
}
//-----------------------------Update-All-Fields------------------------------
function updateAllFields() {
    userUsernameInputValue = (userUsernameInput as HTMLInputElement).value;
    userPasswordInputValue = (userPasswordInput as HTMLInputElement).value;
    userPasswordConfirmInputValue = (userPasswordConfirmInput as HTMLInputElement).value;
    signupTermsCheckboxValue = (signupTermsCheckbox as HTMLInputElement).checked;
}
//------------------------------Has-Empty-Fields------------------------------
function hasEmptyFields() {
    updateAllFields();
    return userUsernameInputValue === '' || userPasswordInputValue === '' ||
        userPasswordConfirmInputValue === '';
}
//-----------------------------Empty-Field-Popup------------------------------
function emptyFieldPopup() {
    if (hasEmptyFields()) {
        popupDiv.style.display = 'block';
        popupText.textContent = 'Please fill out all fields.';
    } else {
        popupText.textContent = '';
        popupDiv.style.display = 'none';
    }
}
//------------------------------Signup-Sequence-------------------------------
function signupSequence() {
    if (!hasEmptyFields() && passwordsMatch() && termsAgreed()) {
        sendSignupDataToServer();
    }
}
//=============================-Event-Listeners-==============================
signupButton.addEventListener('click', signupSequence);
signupButton.addEventListener('click', emptyFieldPopup);
userPasswordConfirmInput.addEventListener('input', passwordMatchPopup);
signupButton.addEventListener('click', signupTermsPopup);
console.log('Get Started Script Loaded');