package com.gruppo10.controller;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneWriter;
import com.gruppo10.classi.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/*
WORK IN PROGRESS
*/

public class RispostaRecensioneController {

    private Stage stage;

    private Recensione recensione;
    
    @FXML private TextArea txtRisposta;
    
    @FXML private Button btnAnnulla;
    
    @FXML private Button btnInvia;

    @FXML
    private void initialize() {

        // Aggiungi listener per abilitare/disabilitare il pulsante
        txtRisposta.textProperty().addListener((_, _, _) -> checkFields());
        checkFields();
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void setRecensione(Recensione recensione){
        this.recensione = recensione;
    }


    private void checkFields() {
        // Controlla se tutti i campi sono riempiti
        boolean allFieldsFilled = !txtRisposta.getText().isEmpty();
                                  
        // Abilita o disabilita il pulsante in base ai campi
        btnInvia.setDisable(!allFieldsFilled);
    }


    @FXML
    private void aggiungiRisposta() throws Exception {

        // Recupera i dati dai campi
        String risposta = txtRisposta.getText();

        // Aggiungere risposta a csv
        try {
            RecensioneWriter.aggiungiRisposta(this.recensione, risposta);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Chiudi la finestra o esegui altre azioni
        if (stage != null) {
            annulla();
        }
    }
    
    
    @FXML
    private void annulla() {
        SceneManager.annulla(btnAnnulla);
    }
}