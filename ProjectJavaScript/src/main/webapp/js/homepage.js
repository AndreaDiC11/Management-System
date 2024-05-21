document.addEventListener('DOMContentLoaded', function () {
    const addSubfolderButtons = document.querySelectorAll('.add-subfolder-btn');
    const addDocumentButtons = document.querySelectorAll('.add-document-btn');

    addSubfolderButtons.forEach(button => {
        button.addEventListener('click', function () {
            const inputDiv = button.closest('.action-buttons').nextElementSibling;
            inputDiv.style.display = inputDiv.style.display === 'none' ? 'block' : 'none';
        });
    });

    addDocumentButtons.forEach(button => {
        button.addEventListener('click', function () {
            const inputDiv = button.closest('.action-buttons').nextElementSibling.nextElementSibling;
            inputDiv.style.display = inputDiv.style.display === 'none' ? 'block' : 'none';
        });
    });

    const saveSubfolderButtons = document.querySelectorAll('.save-subfolder-btn');
    const saveDocumentButtons = document.querySelectorAll('.save-document-btn');

    saveSubfolderButtons.forEach(button => {
        button.addEventListener('click', function () {
            const inputDiv = button.parentElement;
            const inputField = inputDiv.querySelector('input');
            const subfolderName = inputField.value;
            const parentId = button.closest('li').querySelector('.add-subfolder-btn').dataset.folderId;

            if (subfolderName) {
                fetch('CreaSottoCartella', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: `parentId=${parentId}&folderName=${encodeURIComponent(subfolderName)}`
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

    saveDocumentButtons.forEach(button => {
        button.addEventListener('click', function () {
            const inputDiv = button.parentElement;
            const documentNameField = inputDiv.querySelector('input[type="text"]');
            const documentDateField = inputDiv.querySelector('input[type="date"]');
            const documentTypeField = inputDiv.querySelector('input[placeholder="Tipo documento"]');
            const documentSummaryField = inputDiv.querySelector('textarea');
            const documentName = documentNameField.value;
            const documentDate = documentDateField.value;
            const documentType = documentTypeField.value;
            const documentSummary = documentSummaryField.value;
            const parentId = button.closest('li').querySelector('.add-document-btn').dataset.folderId;

            if (documentName && documentDate && documentType && documentSummary) {
                fetch('CreaDocumento', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: `parentId=${parentId}&documentName=${encodeURIComponent(documentName)}&documentDate=${encodeURIComponent(documentDate)}&documentType=${encodeURIComponent(documentType)}&documentSummary=${encodeURIComponent(documentSummary)}`
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
                alert('Compila tutti i campi per il documento');
            }
        });
    });
});