package com.gruppo10.controller;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneWriter;
import com.gruppo10.classi.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class RispostaRecensioneController {

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

        String risposta = txtRisposta.getText();

        try {
            RecensioneWriter.aggiungiRisposta(this.recensione, risposta);
        } catch (Exception e) {
            e.printStackTrace();
        }

        chiudi();
    }
    
    
    @FXML
    private void chiudi() {
        SceneManager.chiudi(btnAnnulla);
    }
}