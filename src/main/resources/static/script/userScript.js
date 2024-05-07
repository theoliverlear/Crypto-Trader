let signupContentContainer = document.getElementById('signup-form-container');
let loginContentContainer = document.getElementById('login-form-container');
let signupTabSelector = document.getElementById('signup-tab-selector');
let loginTabSelector = document.getElementById('login-tab-selector');
let selectors = [signupTabSelector, loginTabSelector];
function toggleUserInfoContainer() {
    console.log('toggleUserInfoContainer');
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
selectors.forEach(selector => {
    selector.addEventListener('click', toggleUserInfoContainer)
});



let signupUsernameInput = document.getElementById('signup-username-input');
let signupPasswordInput = document.getElementById('signup-password-input');
let signupConfirmPasswordInput = document.getElementById('signup-confirm-password-input');

let loginUsernameInput = document.getElementById('login-username-input');
let loginPasswordInput = document.getElementById('login-password-input');


let signupButton = document.getElementById('signup-button-container');
let loginButton = document.getElementById('login-button-container');
function sendSignUpToServer() {

}
function sendLoginToServer() {

}