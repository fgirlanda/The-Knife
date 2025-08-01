package com.gruppo10.controller;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneWriter;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/*
WORK IN PROGRESS
*/

public class ModificaRecensioneController {

    private Utente utenteLoggato = LoginController.utenteLoggato;

    private Stage stage;

    private Ristorante ristorante;
    
    @FXML private TextArea txtTesto;
    
    @FXML private Button btnAnnulla;
    
    @FXML private Button btnInvia;

    @FXML private RadioButton radioStella1;

    @FXML private RadioButton radioStella2;
    
    @FXML private RadioButton radioStella3;

    @FXML private RadioButton radioStella4;

    @FXML private RadioButton radioStella5;

    @FXML private ToggleGroup stelleGroup;
    

    @FXML
    private void initialize() {

        // Aggiungi listener per abilitare/disabilitare il pulsante
        txtTesto.textProperty().addListener((_, _, _) -> checkFields());

        checkFields();
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void setRistorante(Ristorante ristorante){
        this.ristorante = ristorante;
    }


    private void checkFields() {
        // Controlla se tutti i campi sono riempiti
        boolean allFieldsFilled = !txtTesto.getText().isEmpty() &&
                                  stelleGroup.getSelectedToggle() != null;
                                  
        // Abilita o disabilita il pulsante in base ai campi
        btnInvia.setDisable(!allFieldsFilled);
    }

    // @FXML
    // private void modificaRecensione(){
    //     RecensioneWriter.modificaRecensione(this.recensione, testoModificato);
    // }
    
    @FXML
    private void annulla() {
        SceneManager.annulla(btnAnnulla);
    }
}