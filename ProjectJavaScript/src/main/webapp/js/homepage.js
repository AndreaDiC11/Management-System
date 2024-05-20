document.addEventListener('DOMContentLoaded', function () {
    // Seleziona tutti i pulsanti "AGGIUNGI SOTTOCARTELLA"
    const addSubfolderButtons = document.querySelectorAll('.add-subfolder-btn');

    addSubfolderButtons.forEach(button => {
        button.addEventListener('click', function () {
            // Trova il div del campo di input corrispondente
            const inputDiv = button.nextElementSibling;
            inputDiv.style.display = inputDiv.style.display === 'none' ? 'block' : 'none';
        });
    });

    // Seleziona tutti i pulsanti "Salva"
    const saveSubfolderButtons = document.querySelectorAll('.save-subfolder-btn');

    saveSubfolderButtons.forEach(button => {
        button.addEventListener('click', function () {
            const inputDiv = button.parentElement;
            const inputField = inputDiv.querySelector('input');
            const subfolderName = inputField.value;
            const folderId = button.closest('li').querySelector('.add-subfolder-btn').dataset.folderId;

            if (subfolderName) {
                // Invia una richiesta per creare la nuova sottocartella (usando fetch API)
                fetch('CreaSottoCartella', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: `parentFolder=${folderId}&folderName=${encodeURIComponent(subfolderName)}`
                })
                .then(response => {
                    if (response.ok) {
                        // Ricarica la pagina dopo aver aggiunto la sottocartella con successo
                        window.location.reload();
                    } else {
                        // Gestisci l'errore
                        throw new Error('Errore durante la creazione della sottocartella');
                    }
                })
                .catch(error => {
                    console.error('Errore:', error);
                    alert('Errore durante la creazione della sottocartella');
                });
            } else {
                alert('Inserisci un nome per la sottocartella');
            }
        });
    });
});
