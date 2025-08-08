package com.gruppo10.controller;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneCSV;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;

public class AggiungiRecensioneController extends Controller {

    private Utente utenteLoggato = LoginController.utenteLoggato;

    private Ristorante ristorante;

    @FXML
    private TextArea txtTesto;

    @FXML
    private Button btnInvia;

    @FXML
    private RadioButton radioStella1;

    @FXML
    private RadioButton radioStella2;

    @FXML
    private RadioButton radioStella3;

    @FXML
    private RadioButton radioStella4;

    @FXML
    private RadioButton radioStella5;

    @FXML
    private ToggleGroup stelleGroup;

    @FXML
    private void initialize() {
        // Aggiungi listener per abilitare/disabilitare il pulsante
        txtTesto.textProperty().addListener((_, _, _) -> controllaCampi());
    }


    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;
    }

    private void controllaCampi() {
        boolean campiVuoti = txtTesto.getText().isBlank() ||
                stelleGroup.getSelectedToggle() == null;

        disabilitaBottone(btnInvia, campiVuoti);
    }

    @FXML
    private void aggiungiRecensione(){

        // Recupera i dati dai campi
        String testo = txtTesto.getText();
        RadioButton selectedStella = (RadioButton) stelleGroup.getSelectedToggle();
        int stelle = selectedStella.getText().length();

        // Crea un oggetto recensione
        Recensione recensione = new Recensione();
        recensione.setUsername(utenteLoggato.getUsername());
        recensione.setIdUtente(utenteLoggato.getId());
        recensione.setIdRis(ristorante.getId());
        recensione.setStelle(stelle);
        recensione.setTesto(testo);
        recensione.setRisposta("");

        ristorante.aggiungiRecensione(recensione);

        try {
            RecensioneCSV recensioneCSV = new RecensioneCSV();
            recensioneCSV.scrivi(recensione);
        } catch (Exception e) {
            return;
        }
        SceneManager.apriPaginaRistorante(stage, ristorante, paginaPrincipale);
        chiudi();
    }
}