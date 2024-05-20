document.addEventListener('DOMContentLoaded', function () {
    const addSubfolderButtons = document.querySelectorAll('.add-subfolder-btn');

    addSubfolderButtons.forEach(button => {
        button.addEventListener('click', function () {
            const inputDiv = button.nextElementSibling;
            inputDiv.style.display = inputDiv.style.display === 'none' ? 'block' : 'none';
        });
    });

    const saveSubfolderButtons = document.querySelectorAll('.save-subfolder-btn');

    saveSubfolderButtons.forEach(button => {
        button.addEventListener('click', function () {
            const inputDiv = button.parentElement;
            const inputField = inputDiv.querySelector('input');
            const subfolderName = inputField.value;
            const folderId = button.closest('li').querySelector('.add-subfolder-btn').dataset.folderId;

            if (subfolderName) {
                fetch('CreaSottoCartella', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: `parentId=${folderId}&folderName=${encodeURIComponent(subfolderName)}`
                })
                .then(response => {
                    if (response.ok) {
                        window.location.reload();
                    } else {
                        return response.text().then(errorMessage => {
                            throw new Error(errorMessage);
                        });
                    }
                })
                .catch(error => {
                    console.error('Errore:', error);
                    alert(error.message);
                });
            } else {
                alert('Inserisci un nome per la sottocartella');
            }
        });
    });
});
