package com.gruppo10.controller;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneWriter;
import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class AggiungiRecensioneController {

    private Utente utenteLoggato = LoginController.utenteLoggato;

    private Stage stage;

    // @FXML
    // variabile per voto
    
    @FXML
    private TextArea txtTesto;
    
    @FXML
    private Button btnAnnulla;
    
    @FXML
    private Button btnAggiungiRecensione;
    

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {

        // Aggiungi listener per abilitare/disabilitare il pulsante
        txtTesto.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        // voto.textProperty().addListener((observable, oldValue, newValue) -> checkFields());

        checkFields();
    }

    private void checkFields() {

        // Controlla se tutti i campi sono riempiti
        boolean allFieldsFilled = !txtTesto.getText().isEmpty(); //&& controllo voto
                                  
        // Abilita o disabilita il pulsante in base ai campi
        btnAggiungiRecensione.setDisable(!allFieldsFilled);
    }

    @FXML
    private void aggiungiRecensione() throws Exception {

        // Recupera i dati dai campi
        String testo = txtTesto.getText();
        //recupero voto

        // Verifica che tutti i campi obbligatori siano compilati
        if (testo.isEmpty()) { //|| voto
            System.out.println("Compila tutti i campi obbligatori!");
            return;
        }

        // Crea un oggetto recensione
        Recensione recensione = new Recensione();
        // recensione.setIdRec(0); mettere solo nel csv e quando si scrive controllare l'ultimo presente?
        recensione.setIdUtente(utenteLoggato.getId());
        // recensione.setIdRis() id ristoranteAperto (simile a utenteLoggato?)
        recensione.setTesto(testo);

        RecensioneWriter writer = new RecensioneWriter();
        try {
            writer.scriviRecensione(recensione);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Chiudi la finestra o esegui altre azioni
        if (stage != null) {
            stage.close();
        }
    }
    
    @FXML
    private void annulla() {
        stage.close(); // Chiude la finestra
    }
}