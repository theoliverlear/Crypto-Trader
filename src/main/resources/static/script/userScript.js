//================================-Variables-=================================

//-----------------------------------Signup-----------------------------------
let signupContentContainer = document.getElementById('signup-form-container');
let signupTabSelector = document.getElementById('signup-tab-selector');
let signupUsernameInput = document.getElementById('signup-username-input');
let signupPasswordInput = document.getElementById('signup-password-input');
let signupConfirmPasswordInput = document.getElementById('signup-confirm-password-input');
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
//=============================-Server-Functions-=============================

//---------------------------Send-Sign-Up-To-Server---------------------------
function sendSignUpToServer() {
    fetch('/user/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: signupUsernameInput.value,
            password: signupPasswordInput.value,
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
    fetch('/user/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: loginUsernameInput.value,
            password: loginPasswordInput.value,
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

//-------------------------Toggle-User-Info-Container-------------------------
function toggleUserInfoContainer() {
    let isCurrentlySignUp = signupContentContainer.display === 'flex';
    let isCurrentlyLogIn = loginContentContainer.display === 'flex';
    let clickedId = this.id;
    if (clickedId === 'signup-tab-selector' && !isCurrentlySignUp) {
        signupContentContainer.style.display = 'flex';
        loginContentContainer.style.display = 'none';
    } else if (clickedId === 'login-tab-selector' && !isCurrentlyLogIn) {
        signupContentContainer.style.display = 'none';
        loginContentContainer.style.display = 'flex';
    }
}

//=============================-Event-Listeners-==============================
selectors.forEach(selector => {
    selector.addEventListener('click', toggleUserInfoContainer)
});
signupButton.addEventListener('click', sendSignUpToServer);
loginButton.addEventListener('click', sendLoginToServer);
