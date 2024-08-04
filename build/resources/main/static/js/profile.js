    document.addEventListener('DOMContentLoaded', function() {
    const usernameError = document.getElementById('usernameError');
    const lengthError = document.getElementById('usernameLengthError');
    if (usernameError || lengthError) {
    editBtnClick();
}
});
    function editBtnClick(){
    document.getElementById('change-username').classList.remove('hidden');
}

