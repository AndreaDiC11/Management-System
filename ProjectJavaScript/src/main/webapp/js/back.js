// Seleziona il pulsante
    let backButton = document.getElementById('backButton');

    // Aggiungi un event listener per gestire il click sul pulsante
    backButton.addEventListener('click', function() {
      // Usa history.back() per tornare alla pagina precedente
      window.history.back();
    });