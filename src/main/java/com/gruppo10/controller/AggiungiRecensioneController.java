/*
 * Francesco Girlanda 760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
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

/**
 * Controller per la vista che permette agli utenti di aggiungere una recensione
 * a un ristorante. Gestisce l'interazione con gli elementi dell'interfaccia utente
 * per la selezione del voto e l'inserimento del testo della recensione,
 * e si occupa del salvataggio dei dati.
 */
public class AggiungiRecensioneController extends BasicController {

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

    /**
     * Metodo di inizializzazione chiamato automaticamente dal framework JavaFX
     * dopo che tutti gli elementi FXML sono stati iniettati.
     * Aggiunge un listener al campo di testo per controllare lo stato dei campi
     * e abilitare/disabilitare il pulsante di invio.
     */
    @FXML
    private void initialize() {
        // Aggiungi listener per abilitare/disabilitare il pulsante
        txtTesto.textProperty().addListener((_, _, _) -> controllaCampi());
    }


    /**
     * Imposta il ristorante per il quale si sta scrivendo la recensione.
     * Questo metodo viene chiamato dall'esterno, tipicamente dal controller della
     * pagina del ristorante.
     *
     * @param ristorante l'oggetto {@link Ristorante} a cui aggiungere la recensione.
     */
    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;
    }

    /**
     * Controlla se i campi necessari (testo e voto) sono stati compilati.
     * Abilita o disabilita il pulsante di invio di conseguenza.
     */
    private void controllaCampi() {
        boolean campiVuoti = txtTesto.getText().isBlank() ||
                stelleGroup.getSelectedToggle() == null;

        disabilitaBottone(btnInvia, campiVuoti);
    }

    /**
     * Gestisce l'evento di clic sul pulsante di invio.
     * Recupera i dati inseriti dall'utente, crea un nuovo oggetto {@link Recensione},
     * lo aggiunge all'oggetto ristorante e lo salva nel file CSV tramite
     * {@link RecensioneCSV}. Infine, torna alla pagina del ristorante.
     */
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
        recensione.setIdRistorante(ristorante.getId());
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
        SceneManager.apriPaginaRistorante(stage, ristorante, paginaPrincipale, indiceTab);
        chiudi();
    }
}