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
                .then(response => response.json())
                .then(data => {
                    if (data.status === 'success') {
                        // Aggiorna dinamicamente il DOM con la nuova sottocartella
                        const newFolder = document.createElement('li');
                        newFolder.innerHTML = `
                            <a href="Contenuti?folderId=${data.folderId}">${subfolderName}</a>
                            <div class="action-buttons">
                                <button type="button" class="add-subfolder-btn" data-folder-id="${data.folderId}">AGGIUNGI SOTTOCARTELLA</button>
                                <button type="button" class="add-document-btn" data-folder-id="${data.folderId}">AGGIUNGI DOCUMENTO</button>
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
                            </div>`;
                        inputDiv.closest('ul').appendChild(newFolder);
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
                .then(response => response.json())
                .then(data => {
                    if (data.status === 'success') {
 						// Aggiorna dinamicamente il DOM con il nuovo documento
						//alert(parentId);
					    //Crea un nuovo elemento <li> per il nuovo documento
						const documentList = button.closest('li').querySelector('ul');
						if (documentList){
							
					    	const newDocumentItem = document.createElement('li');
						
						    //Aggiungi il nome del nuovo documento all'elemento <li>
						    newDocumentItem.innerHTML = `<span>${documentName}</span>`;
						
						    //Aggiungi l'elemento <li> alla lista dei documenti
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
        });
    });
});
