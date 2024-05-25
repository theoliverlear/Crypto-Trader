//=================================-Imports-==================================
import {hashPassword, loadPage} from "./globalScript";
//================================-Variables-=================================

//-----------------------------------Inputs-----------------------------------
let userUsernameInput = document.getElementById('signup-username-input');
let userPasswordInput = document.getElementById('signup-password-input');
let userPasswordConfirmInput = document.getElementById('signup-confirm-password-input');
let signupTermsCheckbox = document.getElementById('signup-terms-agree-checkbox');
//-----------------------------------Popup------------------------------------
let popupDiv = document.getElementById('signup-prompt-popup-div');
let popupText = document.getElementById('signup-prompt-popup-text');
let signupButton = document.getElementById('signup-button-container');
//=============================-Server-Functions-=============================

//-------------------------Send-Signup-Data-To-Server-------------------------
function sendSignupDataToServer() {
    let hashedPassword = hashPassword((userPasswordInput as HTMLInputElement).value);
    console.log(hashedPassword);
    fetch('/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: (userUsernameInput as HTMLInputElement).value,
            password: hashedPassword
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
    let userPasswordInputValue: string = (userPasswordInput as HTMLInputElement).value;
    let userPasswordConfirmInputValue: string = (userPasswordConfirmInput as HTMLInputElement).value;
    return userPasswordInputValue === userPasswordConfirmInputValue;
}
//--------------------------------Terms-Agreed--------------------------------
function termsAgreed() {
    let signupTermsCheckboxValue: boolean = (signupTermsCheckbox as HTMLInputElement).checked;
    return signupTermsCheckboxValue;
}
//------------------------------Has-Empty-Fields------------------------------
function hasEmptyFields() {
    return (userUsernameInput as HTMLInputElement).value === '' || (userPasswordInput as HTMLInputElement).value === '' ||
        (userPasswordConfirmInput as HTMLInputElement).value === '';
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
if (loadPage(document.body, 'get-started')) {
    signupButton.addEventListener('click', signupSequence);
    signupButton.addEventListener('click', emptyFieldPopup);
    userPasswordConfirmInput.addEventListener('input', passwordMatchPopup);
    signupButton.addEventListener('click', signupTermsPopup);
}