    document.addEventListener('DOMContentLoaded', function() {
    const errorElement = document.getElementById('usernameError');
    if (errorElement) {
    editBtnClick();
}
});
    function editBtnClick(){
    document.getElementById('change-username').classList.remove('hidden');
}

