//=================================-Imports-==================================
import {loadPage, logoutSequence} from "./globalScript";
import {createUppy} from "./imageUploadScript";

//================================-Variables-=================================

//----------------------------------Buttons-----------------------------------
let accountLogoutButton = document.getElementById('logout-button-div');
let changePictureButton = $('#profile-picture-input-div');
let profilePictureEditDiv = $('#profile-picture-edit-div');
let profilePictureInput = $('#profile-picture-input');


function hideProfilePictureButton(): void {
    console.log('hideProfilePictureButton');
    changePictureButton.hide();
}
function showProfilePictureButton(): void {
    console.log('showProfilePictureButton');
    changePictureButton.show();
}
function uploadProfilePicture(uploadEvent: Event): void {
    let uppy = createUppy('#profile-picture-edit-dashboard');
    let uploadFile = (uploadEvent.target as HTMLInputElement).files[0];
    uppy.addFile({
        name: uploadFile.name,
        type: uploadFile.type,
        data: uploadFile
    });
}
function uploadImageSequence(uploadEvent: Event): void {
    hideProfilePictureButton();
    uploadProfilePicture(uploadEvent);
    showProfilePictureButton();
}

//=============================-Event-Listeners-==============================
if (loadPage(document.body, 'account')) {
    accountLogoutButton.addEventListener('click', logoutSequence);
    profilePictureInput.on('change', uploadImageSequence);
}
