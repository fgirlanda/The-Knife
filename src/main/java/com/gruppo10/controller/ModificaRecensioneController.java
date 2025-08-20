/*
 * Francesco Girlanda  760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneCSV;
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

/**
 * Controller per la vista che permette agli utenti di modificare una recensione esistente.
 * Gestisce l'interazione con gli elementi dell'interfaccia utente per l'aggiornamento
 * del testo e del voto della recensione, e si occupa del salvataggio delle modifiche
 * su file CSV.
 */
public class ModificaRecensioneController extends BasicController {

    private Recensione recensione;

    private Ristorante ristorante;

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

    /**
     * Metodo di inizializzazione chiamato automaticamente dal framework JavaFX
     * dopo che tutti gli elementi FXML sono stati iniettati.
     * Aggiunge un listener al campo di testo per controllare lo stato dei campi
     * e abilitare/disabilitare il pulsante di invio.
     */
    @FXML
    private void initialize() {

        // Aggiungi listener per abilitare/disabilitare il pulsante
        txtTestoModificato.textProperty().addListener((_, _, _) -> controllaCampi());
    }

    /**
     * Imposta il ristorante a cui appartiene la recensione.
     *
     * @param ristorante l'oggetto {@link Ristorante} della recensione.
     */
    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;
    }

    /**
     * Imposta la recensione da modificare e popola i campi dell'interfaccia utente
     * con i dati esistenti.
     *
     * @param recensione  l'oggetto {@link Recensione} da modificare.
     * @param contenitore il {@link VBox} che contiene la card della recensione, se applicabile.
     */
    public void setRecensione(Recensione recensione, VBox contenitore) {
        this.recensione = recensione;

        username = recensione.getUsername();
        idUt = recensione.getIdUtente();
        idRis = recensione.getIdRistorante();
        vecchioVoto = recensione.getStelle();
        testoOriginale = recensione.getTesto();
        risposta = recensione.getRisposta();

        txtOriginale.setText(testoOriginale);
        ObservableList<Toggle> toggles = stelleGroup.getToggles();
        Toggle toggle = toggles.get(vecchioVoto - 1);
        stelleGroup.selectToggle(toggle);
    }

    /**
     * Controlla se i campi necessari (testo e voto) sono stati compilati.
     * Abilita o disabilita il pulsante di invio di conseguenza.
     */
    private void controllaCampi() {
        boolean campiVuoti = txtTestoModificato.getText().isEmpty() ||
                stelleGroup.getSelectedToggle() == null;

        disabilitaBottone(btnInvia, campiVuoti);
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Modifica".
     * Recupera il testo e il voto modificati, crea un nuovo oggetto {@link Recensione}
     * con i dati aggiornati, rimuove la vecchia recensione e aggiunge quella nuova
     * all'oggetto ristorante. Salva infine le modifiche nel file CSV.
     */
    @FXML
    private void modificaRecensione() {
        String testoModificato = txtTestoModificato.getText();
        RadioButton selectedStella = (RadioButton) stelleGroup.getSelectedToggle();

        int nuovoVoto = selectedStella.getText().length();

        if ((testoModificato != null && !testoModificato.isBlank() && !testoModificato.equals(testoOriginale)) || nuovoVoto != vecchioVoto) {
            Recensione nuovaRecensione = new Recensione();
            nuovaRecensione.setIdRistorante(idRis);
            nuovaRecensione.setIdUtente(idUt);
            nuovaRecensione.setUsername(username);
            nuovaRecensione.setRisposta(risposta);
            nuovaRecensione.setStelle(nuovoVoto);
            
            String nuovoTesto = testoModificato.isBlank() ? testoOriginale : testoModificato;
            nuovaRecensione.setTesto(nuovoTesto);

            ristorante.rimuoviRecensione(recensione);
            ristorante.aggiungiRecensione(nuovaRecensione);

            RecensioneCSV recensioneCSV = new RecensioneCSV();
            recensioneCSV.modificaRecensione(nuovaRecensione, nuovoTesto, nuovoVoto);
        }

        SceneManager.apriPaginaRistorante(stage, this.ristorante, paginaPrincipale, indiceTab);
        chiudi();
    }
}