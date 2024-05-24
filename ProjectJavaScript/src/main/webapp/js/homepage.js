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

    // Definizione della funzione saveSubfolder
function saveSubfolder(button) {
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
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                // Aggiorna dinamicamente il DOM con la nuova sottocartella
                const folderList = button.closest('li').querySelector('[th\\:each]');
                if (folderList) {
                    const newFolderItem = document.createElement('li');
                    newFolderItem.innerHTML = `
                        <span>${subfolderName}</span>
                        <div class="action-buttons">
                            <button type="button" class="add-subfolder-btn" th:data-folder-id="${data.folderId}">AGGIUNGI SOTTOCARTELLA</button>
                            <button type="button" class="add-document-btn" th:data-folder-id="${data.folderId}">AGGIUNGI DOCUMENTO</button>
                        </div>
                        <div class="subfolder-input" style="display:none;">
                            <input type="text" placeholder="Nome sottocartella">
                            <button type="button" class="save-subfolder-btn">Salva</button>
                        </div>
                        <div class="document-input" style="display:none;">
                            <input type="text" placeholder="Nome documento">
                            <input type="date" placeholder="Data documento">
                            <input type="text" placeholder="Tipo documento">
                            <textarea placeholder="Sommario documento"></textarea>
                            <button type="button" class="save-document-btn">Salva</button>
                        </div>
                        <ul></ul>`;
                    folderList.appendChild(newFolderItem);

                    // Aggiungi l'evento click per la nuova sottocartella
                    const newAddSubfolderBtn = newFolderItem.querySelector('.add-subfolder-btn');
                    if (newAddSubfolderBtn) {
                        newAddSubfolderBtn.addEventListener('click', function () {
                            saveSubfolder(newAddSubfolderBtn);
                        });
                    }

                    // Aggiungi l'evento click per il nuovo documento
                    const newAddDocumentBtn = newFolderItem.querySelector('.add-document-btn');
                    if (newAddDocumentBtn) {
                        newAddDocumentBtn.addEventListener('click', function () {
                            saveDocument(newAddDocumentBtn);
                        });
                    }
                } else {
                    // Se non c'è una lista, crea una nuova
                    const folderList1 = document.createElement('ul');
                    button.closest('li').appendChild(folderList1);
                    const newFolder = document.createElement('li');
                    newFolder.innerHTML = `
                        <span>${subfolderName}</span>
                        <div class="action-buttons">
                            <button type="button" class="add-subfolder-btn" th:data-folder-id="${data.folderId}">AGGIUNGI SOTTOCARTELLA</button>
                            <button type="button" class="add-document-btn" th:data-folder-id="${data.folderId}">AGGIUNGI DOCUMENTO</button>
                        </div>
                        <div class="subfolder-input" style="display:none;">
                            <input type="text" placeholder="Nome sottocartella">
                            <button type="button" class="save-subfolder-btn">Salva</button>
                        </div>
                        <div class="document-input" style="display:none;">
                            <input type="text" placeholder="Nome documento">
                            <input type="date" placeholder="Data documento">
                            <input type="text" placeholder="Tipo documento">
                            <textarea placeholder="Sommario documento"></textarea>
                            <button type="button" class="save-document-btn">Salva</button>
                        </div>
                        <ul></ul>`;
                    folderList1.appendChild(newFolder);

                    // Aggiungi l'evento click per la nuova sottocartella
                    const newAddSubfolderBtn = newFolder.querySelector('.add-subfolder-btn');
                    if (newAddSubfolderBtn) {
                        newAddSubfolderBtn.addEventListener('click', function () {
                            saveSubfolder(newAddSubfolderBtn);
                        });
                    }

                    // Aggiungi l'evento click per il nuovo documento
                    const newAddDocumentBtn = newFolder.querySelector('.add-document-btn');
                    if (newAddDocumentBtn) {
                        newAddDocumentBtn.addEventListener('click', function () {
                            saveDocument(newAddDocumentBtn);
                        });
                    }
                	}

	                // Rimuovi il pulsante "AGGIUNGI SOTTOCARTELLA"
	                const addDocumentBtn = button.closest('li').querySelector('.add-document-btn');
	                if (addDocumentBtn) {
	                    addDocumentBtn.remove();
	                }
	
	                // Resetta i campi di input
	                inputField.value = '';
	                inputDiv.style.display = 'none';
		            } else {
		                alert(data.message);
		            }
	        })
	        .catch(error => {
	            console.error('Errore:', error);
	            alert(error.message);
		        });
	    } else {
	        alert('Inserisci un nome per la sottocartella');
	    }
	}

	// Utilizzo della funzione saveSubfolder all'interno del ciclo forEach
	saveSubfolderButtons.forEach(button => {
	    button.addEventListener('click', function () {
	        saveSubfolder(button);
	    });
	});


// Definizione della funzione saveDocument
function saveDocument(button) {
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
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                // Aggiorna dinamicamente il DOM con il nuovo documento
                const documentList = button.closest('li').querySelector('ul');
                if (documentList) {
                    const newDocumentItem = document.createElement('li');
                    newDocumentItem.innerHTML = `<span>${documentName}</span>`;
                    documentList.appendChild(newDocumentItem);
                } else {
                    // Creare la lista ul
                    const documentList1 = document.createElement('ul');
                    // Aggiungere la lista al parente appropriato
                    button.closest('li').appendChild(documentList1);
                    const newDocument = document.createElement('li');
                    newDocument.innerHTML = `<span>${documentName}</span>`;
                    documentList1.appendChild(newDocument);
                }

                // Rimuovi il pulsante "AGGIUNGI SOTTOCARTELLA"
                const addSubfolderBtn = button.closest('li').querySelector('.add-subfolder-btn');
                if (addSubfolderBtn) {
                    addSubfolderBtn.remove();
                }

                // Resetta i campi di input
                documentNameField.value = '';
                documentDateField.value = '';
                documentTypeField.value = '';
                documentSummaryField.value = '';
                inputDiv.style.display = 'none';
            } else {
                alert(data.message);
            }
        })
        .catch(error => {
            console.error('Errore:', error);
            alert(error.message);
        });
    } else {
        alert('Compila tutti i campi per il documento');
    }
}

	// Utilizzo della funzione saveDocument all'interno del ciclo forEach
	saveDocumentButtons.forEach(button => {
	    button.addEventListener('click', function () {
	        saveDocument(button);
	    });
	});

});
