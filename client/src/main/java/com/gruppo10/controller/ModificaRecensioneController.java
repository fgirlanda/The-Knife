/*
 * Francesco Girlanda  760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import java.sql.SQLException;

import com.gruppo10.classi.GestioneEccezioni;
import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.database.RecensioneDAO;
import com.gruppo10.database.RistoranteDAO;

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
 * Controller per la finestra di dialogo che permette agli utenti di modificare
 * una recensione esistente.
 * Gestisce l'interazione con gli elementi dell'interfaccia utente per
 * l'aggiornamento
 * del testo e del voto della recensione, e si occupa del salvataggio delle
 * modifiche
 * nel database.
 */
public class ModificaRecensioneController extends BasicController {

    /** La recensione da modificare. */
    private Recensione recensione;

    /** Il ristorante a cui appartiene la recensione. */
    private Ristorante ristorante;

    /** Testo originale della recensione prima della modifica. */
    private String testoOriginale;

    /** Username dell'autore della recensione. */
    private String username;

    /** Voto precedente della recensione (numero di stelle). */
    private int vecchioVoto;

    /** ID del ristorante associato alla recensione. */
    private int idRis;

    /** ID dell'utente che ha scritto la recensione. */
    private int idUt;

    /** Risposta eventualmente associata alla recensione. */
    private String risposta;

    /** Campo di testo per mostrare il testo originale della recensione. */
    @FXML
    private Text txtOriginale;

    /** Area di testo per inserire il testo modificato della recensione. */
    @FXML
    private TextArea txtTestoModificato;

    /** Pulsante per inviare le modifiche della recensione. */
    @FXML
    private Button btnInvia;

    /** Radio button per selezionare 1 stella. */
    @FXML
    private RadioButton radioStella1;

    /** Radio button per selezionare 2 stelle. */
    @FXML
    private RadioButton radioStella2;

    /** Radio button per selezionare 3 stelle. */
    @FXML
    private RadioButton radioStella3;

    /** Radio button per selezionare 4 stelle. */
    @FXML
    private RadioButton radioStella4;

    /** Radio button per selezionare 5 stelle. */
    @FXML
    private RadioButton radioStella5;

    /** ToggleGroup per gestire la selezione delle stelle. */
    @FXML
    private ToggleGroup stelleGroup;

    @FXML
    private void initialize() {
        btnInvia.setDefaultButton(true);
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
     * @param contenitore il {@link VBox} che contiene la card della recensione, se
     *                    applicabile.
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

        stelleGroup.selectedToggleProperty().addListener((_, _, _) -> controllaCampi());
        txtTestoModificato.textProperty().addListener((_, _, _) -> controllaCampi());

        controllaCampi();
    }

    /**
     * Controlla se i campi necessari (testo e/o voto) sono stati modificati.
     * Abilita o disabilita il pulsante di invio di conseguenza.
     */
    private void controllaCampi() {
        RadioButton selectedStelle = (RadioButton) stelleGroup.getSelectedToggle();
        int nuovoVoto = selectedStelle.getText().length();
        boolean campiVuoti = txtTestoModificato.getText().isEmpty() &&
                nuovoVoto == vecchioVoto;

        disabilitaBottone(btnInvia, campiVuoti);
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Modifica".
     * Recupera il testo e il voto modificati, crea un nuovo oggetto
     * {@link Recensione}
     * con i dati aggiornati, aggiorna l'oggetto ristorante e affida al DAO il
     * ricalcolo della media dopo aver salvato le modifiche nel database.
     */
    @FXML
    private void modificaRecensione() {
        String testoModificato = txtTestoModificato.getText();
        RadioButton selectedStella = (RadioButton) stelleGroup.getSelectedToggle();

        int nuovoVoto = selectedStella.getText().length();

        String nuovoTesto = testoModificato.isBlank() ? testoOriginale : testoModificato;
        try {
            boolean modificata = new RecensioneDAO().modificaRecensione(
                    recensione.getIdRec(), nuovoTesto, nuovoVoto);
            if (!modificata) {
                GestioneEccezioni.errore("Recensione non trovata",
                        new SQLException("La recensione non esiste più nel database"), false, null);
                return;
            }

            ristorante.rimuoviRecensione(recensione);
            recensione.setTesto(nuovoTesto);
            recensione.setStelle(nuovoVoto);
            recensione.setIdRistorante(idRis);
            recensione.setIdUtente(idUt);
            recensione.setUsername(username);
            recensione.setRisposta(risposta);
            recensione.setRistorante(ristorante);
            ristorante.aggiungiRecensione(recensione);
            new RistoranteDAO().aggiornaMediaRecensioni(ristorante);
        } catch (IllegalArgumentException | SQLException e) {
            GestioneEccezioni.errore("Errore durante la modifica della recensione", e, false, null);
            return;
        }

        SceneManager.apriPaginaRistorante(stage, ristorante, paginaPrincipale, indiceTab);
        chiudi();
    }
}
