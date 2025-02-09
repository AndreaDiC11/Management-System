document.addEventListener('DOMContentLoaded', function () {
    const addSubfolderButtons = document.querySelectorAll('.add-subfolder-btn');
    const addDocumentButtons = document.querySelectorAll('.add-document-btn');
    
	const confirmModal = document.querySelector('.confirmModal');
	const confirmDeleteBtn = document.querySelector('.confirmDeleteBtn');
	const cancelDeleteBtn = document.querySelector('.cancelDeleteBtn');


    addSubfolderButtons.forEach(button => {
        button.addEventListener('click', function () {
			const inputDiv = button.closest('.action-buttons').parentElement.querySelector('.subfolder-input');
            inputDiv.style.display = inputDiv.style.display === 'none' ? 'block' : 'none';
        });
    });

    addDocumentButtons.forEach(button => {
        button.addEventListener('click', function () {
			const inputDiv = button.closest('.action-buttons').parentElement.querySelector('.document-input');
            inputDiv.style.display = inputDiv.style.display === 'none' ? 'block' : 'none';
        });
    });

    const saveSubfolderButtons = document.querySelectorAll('.save-subfolder-btn');
    const saveDocumentButtons = document.querySelectorAll('.save-document-btn');
    

    // Definizione della funzione saveSubfolder
function saveSubfolder(button, parentId) {
    const inputDiv = button.parentElement;
    const inputField = inputDiv.querySelector('input');
    const subfolderName = inputField.value;

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
                const folderElement = document.querySelector(`li[data-folder-id="${parentId}"]`);
			if (folderElement) {
			    // Trova tutti gli elementi HTML che rappresentano le sottocartelle all'interno della cartella
			    const folderList = folderElement.querySelectorAll('li[data-folder-id]');
			    if (folderList.length === 0){
                    // Se non c'è una lista, crea una nuova
                    const folderList1 = document.createElement('ul');
                    button.closest('li').appendChild(folderList1);
                    const newFolder = document.createElement('li');
                    newFolder.setAttribute('data-folder-id', data.folderId); // Aggiungi th:data-folder-id
                    newFolder.setAttribute('data-folder-name', saveSubfolder);
                    newFolder.setAttribute('ondrop', 'handleDrop(event)'); // Aggiungi ondrop
                    newFolder.setAttribute('ondragover', 'handleDragOver(event)'); // Aggiungi ondragover
                    newFolder.setAttribute('draggable', true);
                    newFolder.setAttribute('dragstart', 'handleDragStart(event)'); 
                    newFolder.innerHTML = `
                        <span>${subfolderName}</span>
                        <div class="action-buttons">
                            <button type="button" class="add-subfolder-btn" th:data-folder-id="${data.folderId}">AGGIUNGI SOTTOCARTELLA</button>
                            <button type="button" class="add-document-btn" th:data-folder-id="${data.folderId}">AGGIUNGI DOCUMENTO</button>
                        </div>
                        <div class="subfolder-input" th:data-folder-id="${data.folderId}" style="display:none;">
                            <input type="text" placeholder="Nome sottocartella">
                            <button type="button" class="save-subfolder-btn">Salva</button>
                        </div>
                        <div class="document-input" th:data-folder-id="${data.folderId}" style="display:none;">
                            <input type="text" placeholder="Nome documento">
                            <input type="date" placeholder="Data documento">
                            <input type="text" placeholder="Tipo documento">
                            <textarea placeholder="Sommario documento"></textarea>
                            <button type="button" class="save-document-btn">Salva</button>
                        </div>`;
                    folderList1.appendChild(newFolder);
                    
                    const newAddSubfolderBtn = newFolder.querySelector('.add-subfolder-btn');
			        if (newAddSubfolderBtn) {
			            newAddSubfolderBtn.addEventListener('click', function () {
			                const inputDiv = newAddSubfolderBtn.closest('.action-buttons').parentElement.querySelector('.subfolder-input');
			                inputDiv.style.display = inputDiv.style.display === 'none' ? 'block' : 'none';
			            });
			        }
			
			        const newSaveSubfolderBtn = newFolder.querySelector('.save-subfolder-btn');
			        if (newSaveSubfolderBtn) {
			            newSaveSubfolderBtn.addEventListener('click', function () {
			                saveSubfolder(newSaveSubfolderBtn, data.folderId);
			            });
			        }
			
			        const newAddDocumentBtn = newFolder.querySelector('.add-document-btn');
			        if (newAddDocumentBtn) {
			            newAddDocumentBtn.addEventListener('click', function () {
			                const inputDiv = newAddDocumentBtn.closest('.action-buttons').parentElement.querySelector('.document-input');
			                inputDiv.style.display = inputDiv.style.display === 'none' ? 'block' : 'none';
			            });
			        }
			
			        const newSaveDocumentBtn = newFolder.querySelector('.save-document-btn');
			        if (newSaveDocumentBtn) {
			            newSaveDocumentBtn.addEventListener('click', function () {
			                saveDocument(newSaveDocumentBtn, data.folderId);
			            });
			        }						
				    // Rimuovi Aggiungi Documento			    
                    const folderElement = document.querySelector(`li[data-folder-id="${parentId}"]`);
                    const addDocumentBtn = folderElement.querySelector('.add-document-btn');
                    const DocumentInput = folderElement.querySelector('.document-input');

					
					if (addDocumentBtn && DocumentInput) {
					    addDocumentBtn.disabled = true;
					    DocumentInput.style.display = 'none';
					}
                } else {
                    const newFolder = document.createElement('li');
                    newFolder.setAttribute('data-folder-id', data.folderId); // Aggiungi th:data-folder-id
                    newFolder.setAttribute('data-folder-name', saveSubfolder);
                    newFolder.setAttribute('ondrop', 'handleDrop(event)'); // Aggiungi ondrop
                    newFolder.setAttribute('ondragover', 'handleDragOver(event)'); // Aggiungi ondragover
                    newFolder.setAttribute('draggable', true);
                    newFolder.setAttribute('dragstart', 'handleDragStart(event)'); 
                    newFolder.innerHTML = `
                        <span>${subfolderName}</span>
                        <div class="action-buttons">
                            <button type="button" class="add-subfolder-btn" th:data-folder-id="${data.folderId}">AGGIUNGI SOTTOCARTELLA</button>
                            <button type="button" class="add-document-btn" th:data-folder-id="${data.folderId}">AGGIUNGI DOCUMENTO</button>
                        </div>
                        <div class="subfolder-input" th:data-folder-id="${data.folderId}" style="display:none;">
                            <input type="text" placeholder="Nome sottocartella">
                            <button type="button" class="save-subfolder-btn">Salva</button>
                        </div>
                        <div class="document-input" th:data-folder-id="${data.folderId}" style="display:none;">
                            <input type="text" placeholder="Nome documento">
                            <input type="date" placeholder="Data documento">
                            <input type="text" placeholder="Tipo documento">
                            <textarea placeholder="Sommario documento"></textarea>
                            <button type="button" class="save-document-btn">Salva</button>
                        </div>`;
                    button.closest('li').querySelector('ul').appendChild(newFolder);

                    
                    const newAddSubfolderBtn = newFolder.querySelector('.add-subfolder-btn');
			        if (newAddSubfolderBtn) {
			            newAddSubfolderBtn.addEventListener('click', function () {
			                const inputDiv = newAddSubfolderBtn.closest('.action-buttons').parentElement.querySelector('.subfolder-input');
			                inputDiv.style.display = inputDiv.style.display === 'none' ? 'block' : 'none';
			            });
			        }
			
			        const newSaveSubfolderBtn = newFolder.querySelector('.save-subfolder-btn');
			        if (newSaveSubfolderBtn) {
			            newSaveSubfolderBtn.addEventListener('click', function () {
			                saveSubfolder(newSaveSubfolderBtn, data.folderId);
			            });
			        }
			
			        const newAddDocumentBtn = newFolder.querySelector('.add-document-btn');
			        if (newAddDocumentBtn) {
			            newAddDocumentBtn.addEventListener('click', function () {
			                const inputDiv = newAddDocumentBtn.closest('.action-buttons').parentElement.querySelector('.document-input');
			                inputDiv.style.display = inputDiv.style.display === 'none' ? 'block' : 'none';
			            });
			        }
			
			        const newSaveDocumentBtn = newFolder.querySelector('.save-document-btn');
			        if (newSaveDocumentBtn) {
			            newSaveDocumentBtn.addEventListener('click', function () {
			                saveDocument(newSaveDocumentBtn, data.folderId);
			            });
			        }	
				}


            	
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
	            alert("Si è verificato un errore");
		        });
	    } else {
	        alert('Inserisci un nome per la sottocartella');
	    }
	    
	    
	}

	// Utilizzo della funzione saveSubfolder all'interno del ciclo forEach
	saveSubfolderButtons.forEach(button => {
	    button.addEventListener('click', function () {
	        saveSubfolder(button, button.closest('li').querySelector('.add-subfolder-btn').dataset.folderId);
	    });
	});


// Definizione della funzione saveDocument
function saveDocument(button, parentId) {
    const inputDiv = button.parentElement;
    const documentNameField = inputDiv.querySelector('input[type="text"]');
    const documentDateField = inputDiv.querySelector('input[type="date"]');
    const documentTypeField = inputDiv.querySelector('input[placeholder="Tipo documento"]');
    const documentSummaryField = inputDiv.querySelector('textarea');
    const documentName = documentNameField.value;
    const documentDate = documentDateField.value;
    const documentType = documentTypeField.value;
    const documentSummary = documentSummaryField.value;


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
                    newDocumentItem.setAttribute('draggable', true);
                    newDocumentItem.setAttribute('dragstart', 'handleDragStart(event)'); 
                    newDocumentItem.setAttribute('data-document-id', data.documentId);
                    newDocumentItem.innerHTML = `<span>${documentName}</span>`;
                    
                    const anchor = document.createElement('a');
                    anchor.href = `AccessDocument?documentId=${data.documentId}`;
                    anchor.textContent = 'accedi';
                    newDocumentItem.appendChild(anchor);
                    
                    documentList.appendChild(newDocumentItem);
                } else {
                    // Creare la lista ul
                    const documentList1 = document.createElement('ul');
                    // Aggiungere la lista al parente appropriato
                    button.closest('li').appendChild(documentList1);
                    const newDocument = document.createElement('li');
                    newDocument.setAttribute('draggable', true);
                    newDocument.setAttribute('dragstart', 'handleDragStart(event)'); 
                    newDocument.setAttribute('data-document-id', data.documentId);
                    newDocument.innerHTML = `<span>${documentName}</span>`;
                    
                    const anchor = document.createElement('a');
                    anchor.href = `AccessDocument?documentId=${data.documentId}`;
                    anchor.textContent = ' accedi';
                    newDocument.appendChild(anchor);
                    
                    documentList1.appendChild(newDocument);

					
                }
                
            	//Rimuovi Aggiungi SottoCartella
				const folderElement = document.querySelector(`li[data-folder-id="${parentId}"]`);
                const addSubfolderBtn = folderElement.querySelector('.add-subfolder-btn');
                const subfolderInput = folderElement.querySelector('.subfolder-input');


				if (addSubfolderBtn && subfolderInput) {
				    addSubfolderBtn.disabled = true;
				    subfolderInput.style.display = 'none';
					}
                			
				

                // Resetta i campi di input
                documentNameField.value = '';
                documentDateField.value = '';
                documentTypeField.value = '';
                documentSummaryField.value = '';
                inputDiv.style.display = 'none';
                
                addDragAndDropListeners();
            } else {
                alert(data.message);
            }
        })
        .catch(error => {
            console.error('Errore:', error);
            alert("Si è verificato un errore");
        });
    } else {
        alert('Compila tutti i campi per il documento');
    }
}

	// Utilizzo della funzione saveDocument all'interno del ciclo forEach
	saveDocumentButtons.forEach(button => {
	    button.addEventListener('click', function () {
	        saveDocument(button, button.closest('li').querySelector('.add-document-btn').dataset.folderId);
	    });
	});
	
	function handleDragStart(event) {
        event.dataTransfer.setData('text/plain/documentId', event.target.dataset.documentId);
        event.dataTransfer.setData('text/plain/folderId', event.target.dataset.folderId);
        event.dataTransfer.setData('text/plain/folderName', event.target.dataset.folderName);
        event.dataTransfer.effectAllowed = 'move';
    }

    function handleDragOver(event) {
        event.preventDefault();
        event.dataTransfer.dropEffect = 'move';
    }

    function handleDrop(event) {
        event.preventDefault();
 
        const folderId = event.dataTransfer.getData('text/plain/folderId');
        const folderName = event.dataTransfer.getData('text/plain/folderName');
        const documentId = event.dataTransfer.getData('text/plain/documentId');
        const targetFolderId = event.target.closest('li[data-folder-id]').dataset.folderId;
        const targetFolderName = event.target.closest('li[data-folder-id]').dataset.folderName;
        
        if (folderName !=="Cestino" && folderName !=="Cartella"){
	        if (targetFolderName === "Cestino"){		
				confirmModal.style.display = 'block';
			    confirmDeleteBtn.addEventListener('click', function () {
					deleteElement(documentId, folderId);
				});
			}
			else {
				if (documentId){
					moveDocument(documentId, targetFolderId);
				}
			}
		} else {
			alert("Non puoi spostare questo file");
		}
        
        
    }


	function moveDocument(documentId, targetFolderId){
        fetch('SpostaDocumento', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: `documentId=${documentId}&targetFolderId=${targetFolderId}`
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                const documentElement = document.querySelector(`li[data-document-id="${documentId}"]`);
                const oldParentElement = documentElement.parentElement;
                const targetFolderElement = document.querySelector(`li[data-folder-id="${targetFolderId}"] ul`);
                const targetFolderElement1 = document.querySelector(`li[data-folder-id="${targetFolderId}"]`);

                // Rimuovi il documento dalla vecchia cartella
                oldParentElement.removeChild(documentElement);

                // Aggiungi il documento alla nuova cartella
                if (targetFolderElement) {
                    targetFolderElement.appendChild(documentElement);
                } else {
                    const newUl = document.createElement('ul');
                    newUl.appendChild(documentElement);
                    document.querySelector(`li[data-folder-id="${targetFolderId}"]`).appendChild(newUl);
                }
                
                	//Rimuovi aggiungi SottoCartella	           
                    const addSubfolderBtn = targetFolderElement1.querySelector('.add-subfolder-btn');
                    const subfolderInput = targetFolderElement1.querySelector('.subfolder-input');
					
					if (addSubfolderBtn && subfolderInput) {
					    addSubfolderBtn.disabled = true;
					    subfolderInput.style.display = 'none';
					}
	            
	            
	            // Controlla se la vecchia cartella è vuota e aggiungi il pulsante "AGGIUNGI SOTTOCARTELLA" se è vuota
	            if (oldParentElement.children.length === 0) {
				    const oldFolderId = oldParentElement.closest('li[data-folder-id]').dataset.folderId;
					
					//Attiva Aggiungi SottoCartella
			        const folderElement = document.querySelector(`li[data-folder-id="${oldFolderId}"]`);
                    const addSubfolderBtn = folderElement.querySelector('.add-subfolder-btn');
                    const subfolderInput = folderElement.querySelector('.subfolder-input');

					
					if (addSubfolderBtn && subfolderInput) {
					    addSubfolderBtn.disabled = false;
					    subfolderInput.style.display = 'block';
					}
			        
            }
    	                
                
            } else {
                alert('Errore nello spostamento del documento');
            }
        })
        .catch(error => {
            console.error('Errore:', error);
            alert('Errore nello spostamento del documento');
        });
    }
    
    function deleteElement(documentId, folderId){
		fetch('EliminaElemento', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `documentId=${documentId}&folderId=${folderId}`
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    if (documentId !== "undefined") {
						const documentElement = document.querySelector(`li[data-document-id="${documentId}"]`);
						if (documentElement){
							const oldParentElement = documentElement.parentElement;
                        	oldParentElement.removeChild(documentElement);
        		            // Controlla se la vecchia cartella è vuota e aggiungi il pulsante "AGGIUNGI SOTTOCARTELLA" se è vuota
				            if (oldParentElement.children.length === 0) {
							    const oldFolderId = oldParentElement.closest('li[data-folder-id]').dataset.folderId;
								
								//Attiva Aggiungi SottoCartella
						        const folderElement = document.querySelector(`li[data-folder-id="${oldFolderId}"]`);
			                    const addSubfolderBtn = folderElement.querySelector('.add-subfolder-btn');
			                    const subfolderInput = folderElement.querySelector('.subfolder-input');
			
								
								if (addSubfolderBtn && subfolderInput) {
								    addSubfolderBtn.disabled = false;
								    subfolderInput.style.display = 'block';
								}
						        
			            	}
                    	}              	
                    } else {
                        const folderElement = document.querySelector(`li[data-folder-id="${folderId}"]`);
                        if (folderElement) {
							const oldParentElement = folderElement.parentElement;
                            oldParentElement.removeChild(folderElement);
                            
                            const oldFolderId = oldParentElement.closest('li[data-folder-id]').dataset.folderId;
                            const oldParentFolder = document.querySelector(`li[data-folder-id="${oldFolderId}"]`);
                            const figli = oldParentFolder.querySelectorAll('li[data-folder-id]');
        		            // Controlla se la vecchia cartella è vuota e aggiungi il pulsante "AGGIUNGI DOCUMENTO" se è vuota
				            if (figli.length === 0) {
								
								//Attiva Aggiungi Documento
						        const ParentElement = document.querySelector(`li[data-folder-id="${oldFolderId}"]`);
			                    const addDocumentBtn = ParentElement.querySelector('.add-document-btn');
			                    const documentInput = ParentElement.querySelector('.document-input');
		
								
								if (addDocumentBtn && documentInput) {
								    addDocumentBtn.disabled = false;
								    documentInput.style.display = 'block';
								}
                        	}
                    	}
                	}
                } else {
                    alert('Errore nella cancellazione dell\'elemento');
                }
                confirmModal.style.display = 'none';
            })
            .catch(error => {
                console.error('Errore:', error);
                confirmModal.style.display = 'none';
            });
        }


    function addDragAndDropListeners() {
        const documentItems = document.querySelectorAll('li[data-document-id]');
        const folderItems = document.querySelectorAll('li[data-folder-id]');

        documentItems.forEach(item => {
            item.setAttribute('draggable', true);
            item.addEventListener('dragstart', handleDragStart);
        });

        folderItems.forEach(item => {
            item.setAttribute('draggable', true); // Imposta anche gli elementi di cartella come trascinabili
	        item.addEventListener('dragstart', handleDragStart); // Aggiungi l'evento dragstart
	        item.addEventListener('dragover', handleDragOver);
	        item.addEventListener('drop', handleDrop);
            
        });
    }
    
    
    cancelDeleteBtn.addEventListener('click', function () {
        confirmModal.style.display = 'none';
        itemToDelete = null;
    });

    // Chiamata per aggiungere gli event listeners per la prima volta
    addDragAndDropListeners();
	

});
