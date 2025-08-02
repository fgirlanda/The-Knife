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

public class AggiungiRecensioneController {

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


    @FXML
    private void aggiungiRecensione() throws Exception {

        // Recupera i dati dai campi
        String testo = txtTesto.getText();
        RadioButton selectedStella = (RadioButton) stelleGroup.getSelectedToggle();
        int stelle = selectedStella.getText().length();

        // Crea un oggetto recensione
        Recensione recensione = new Recensione();
        // recensione.setIdRec(0); mettere solo nel csv e quando si scrive controllare l'ultimo presente?
        recensione.setNomeUtente(utenteLoggato.getNome());
        recensione.setIdUtente(utenteLoggato.getId());
        recensione.setIdRis(ristorante.getId());
        recensione.setStelle(stelle);
        recensione.setTesto(testo);
        recensione.setRisposta("");

        // Ricalcolo media recensioni ristorante
        ristorante.aggiungiRecensione(recensione);

        // Aggiungere recensione a csv
        RecensioneWriter writer = new RecensioneWriter();
        try {
            writer.scriviRecensione(recensione);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Chiudi la finestra o esegui altre azioni
        annulla();
        SceneManager.reload(stage, "/GUI/pagina_ristorante.fxml");
    }
    
    
    @FXML
    private void annulla() {
        SceneManager.annulla(btnAnnulla);
    }
}