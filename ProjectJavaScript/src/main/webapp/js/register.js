document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form[action="CheckRegister"]');
    const pwd = document.getElementById('pwd');
    const confirmPwd = document.getElementById('confirmPwd');
    const errorMsg = document.getElementById('errorMsg');
    errorMsg.style.color = 'red';

    form.addEventListener('submit', function (event) {
        if (pwd.value !== confirmPwd.value) {
            event.preventDefault();
            errorMsg.textContent = 'Passwords do not match!';
        } else {
            errorMsg.textContent = '';
        }
    });
});
