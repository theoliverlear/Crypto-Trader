let userUsernameInput = document.getElementById('signup-username-input');
let userUsernameInputValue = userUsernameInput.value;
let userPasswordInput = document.getElementById('signup-password-input');
let userPasswordInputValue = userPasswordInput.value;
let userPasswordConfirmInput = document.getElementById('signup-confirm-password-input');
let userPasswordConfirmInputValue = userPasswordConfirmInput.value;
let popupDiv = document.getElementById('signup-prompt-popup-div');
let popupText = document.getElementById('signup-prompt-popup-text');
function passwordMatchPopup() {
    if (!passwordsMatch()) {
        popupDiv.style.display = 'block';
        popupText.textContent = 'Passwords do not match.';
    } else {
        popupText.textContent = '';
        popupDiv.style.display = 'none';
    }
}
userPasswordConfirmInput.addEventListener('input', passwordMatchPopup);

let signupButton = document.getElementById('signup-button-container');
let signupTermsCheckbox = document.getElementById('signup-terms-agree-checkbox');
let signupTermsCheckboxValue = signupTermsCheckbox.checked;
function signupTermsPopup() {
    console.log(signupTermsCheckboxValue);
    if (!termsAgreed()) {
        popupDiv.style.display = 'block';
        popupText.textContent = 'Please agree to the terms and conditions.';
    } else {
        popupText.textContent = '';
        popupDiv.style.display = 'none';
    }
}
signupButton.addEventListener('click', signupTermsPopup);
function passwordsMatch() {
    userPasswordInputValue = userPasswordInput.value;
    userPasswordConfirmInputValue = userPasswordConfirmInput.value;
    return userPasswordInputValue === userPasswordConfirmInputValue;
}
function termsAgreed() {
    signupTermsCheckboxValue = signupTermsCheckbox.checked;
    return signupTermsCheckboxValue;
}
function updateAllFields() {
    userUsernameInputValue = userUsernameInput.value;
    userPasswordInputValue = userPasswordInput.value;
    userPasswordConfirmInputValue = userPasswordConfirmInput.value;
    signupTermsCheckboxValue = signupTermsCheckbox.checked;
}
function emptyInfo() {
    updateAllFields();
    return userUsernameInputValue === '' || userPasswordInputValue === '' || userPasswordConfirmInputValue === '';
}
function emptyInfoPopup() {
    if (emptyInfo()) {
        popupDiv.style.display = 'block';
        popupText.textContent = 'Please fill out all fields.';
    } else {
        popupText.textContent = '';
        popupDiv.style.display = 'none';
    }
}
signupButton.addEventListener('click', emptyInfoPopup);
function sendSignupData() {
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
            window.location.href = '/portfolio/builder';
        } else {
            popupDiv.style.display = 'block';
            popupText.textContent = 'Username already exists.';
        }
    });
}
function signupChain() {
    if (!emptyInfo() && passwordsMatch() && termsAgreed()) {
        sendSignupData();
    }
}
signupButton.addEventListener('click', signupChain);
