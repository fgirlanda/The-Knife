package com.gruppo10.controller;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneCSV;
// import com.gruppo10.classi.RecensioneWriter;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.SceneManager;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ModificaRecensioneController {

    private Recensione recensione;

    private Stage stage;

    private Ristorante ristorante;

    private boolean paginaPrincipale;

    private String testoOriginale;

    private String username;

    private int vecchioVoto;

    private int idRis;

    private int idUt;

    private String risposta;

    @FXML
    private Text txtOriginale;

    @FXML
    private TextArea txtTestoModificato;

    @FXML
    private Button btnAnnulla;

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
        txtTestoModificato.textProperty().addListener((_, _, _) -> checkFields());

        checkFields();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;
    }

    public void setPrincipale(boolean paginaPrincipale) {
        this.paginaPrincipale = paginaPrincipale;
    }

    public void setRecensione(Recensione recensione, VBox contenitore) {
        this.recensione = recensione;

        username = recensione.getUsername();
        idUt = recensione.getIdUtente();
        idRis = recensione.getIdRis();
        vecchioVoto = recensione.getStelle();
        testoOriginale = recensione.getTesto();
        risposta = recensione.getRisposta();

        txtOriginale.setText(testoOriginale);
        ObservableList<Toggle> toggles = stelleGroup.getToggles();
        Toggle toggle = toggles.get(vecchioVoto - 1);
        stelleGroup.selectToggle(toggle);
    }

    private void checkFields() {
        // Controlla se tutti i campi sono riempiti
        boolean allFieldsFilled = !txtTestoModificato.getText().isEmpty() &&
                stelleGroup.getSelectedToggle() != null;

        // Abilita o disabilita il pulsante in base ai campi
        btnInvia.setDisable(!allFieldsFilled);
    }

    @FXML
    private void modificaRecensione() {
        String testoModificato = txtTestoModificato.getText();
        RadioButton selectedStella = (RadioButton) stelleGroup.getSelectedToggle();

        int nuovoVoto = selectedStella.getText().length();

        if (testoModificato != null && !testoModificato.isBlank()) {
            Recensione nuovaRecensione = new Recensione();
            nuovaRecensione.setIdRis(idRis);
            nuovaRecensione.setIdUtente(idUt);
            nuovaRecensione.setUsername(username);
            nuovaRecensione.setRisposta(risposta);
            nuovaRecensione.setTesto(testoModificato);
            nuovaRecensione.setStelle(nuovoVoto);

            ristorante.rimuoviRecensione(recensione);
            ristorante.aggiungiRecensione(nuovaRecensione);

            RecensioneCSV recensioneCSV = new RecensioneCSV();
            recensioneCSV.modificaRecensione(nuovaRecensione, testoModificato, nuovoVoto);
            // RecensioneWriter.modificaRecensione(this.recensione, testoModificato, nuovoVoto);
        }

        // ristorante.aggiornaMedia(vecchioVoto, nuovoVoto);
        SceneManager.apriPaginaRistorante(stage, this.ristorante, paginaPrincipale);
        chiudi();
    }

    @FXML
    private void chiudi() {
        SceneManager.chiudi(btnAnnulla);
    }
}