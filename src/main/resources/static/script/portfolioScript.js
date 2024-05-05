const currencyDropdownCaret = document.getElementById('currency-dropdown-caret');
const currencyDropdownCaretImage = document.getElementById('currency-dropdown-caret-image');
function changeCaretToHighlight() {
    currencyDropdownCaretImage.src = '../static/images/down_caret_highlight.svg';
}
function changeCaretToBlack() {
    currencyDropdownCaretImage.src = '../static/images/down_caret.svg';
}
currencyDropdownCaret.addEventListener('mouseover', changeCaretToHighlight);
currencyDropdownCaret.addEventListener('mouseout', changeCaretToBlack);
const currencyDropdownButton = document.getElementById('currency-dropdown');
const currencyDropdownSelection = document.getElementById('currency-dropdown-selection');
function toggleCurrencyDropdown() {
    currencyDropdownSelection.classList.toggle('hide');
    currencyDropdownButton.classList.toggle('square-bottom');
}
currencyDropdownButton.addEventListener('click', toggleCurrencyDropdown);
const currencySelectionText = document.getElementById('currency-selection-text');
const currencySelectionOptions = document.getElementsByClassName('currency-dropdown-option');
const currencySelectionOptionsArray = Array.from(currencySelectionOptions);
