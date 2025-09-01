/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
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
import javafx.scene.text.Text;

/**
 * Controller per la gestione della finestra di aggiunta recensione di un ristorante.
 * 
 * <p>Questo controller gestisce l'interazione dell'utente con gli elementi
 * dell'interfaccia grafica per l'inserimento del testo della recensione e la
 * selezione del voto in stelle. Si occupa anche di creare un oggetto {@link Recensione},
 * salvarlo tramite {@link RecensioneCSV} e aggiornare la vista della pagina del ristorante.</p>
 * 
 * <p>Estende {@link BasicController} per ereditare funzionalità comuni come
 * la gestione dell'utente loggato, dei pulsanti e delle finestre.</p>
 */
public class AggiungiRecensioneController extends BasicController {

    /** L'utente attualmente loggato nell'applicazione. */
    private Utente utenteLoggato = LoginController.utenteLoggato;

    /** Il ristorante a cui verrà aggiunta la recensione. */
    private Ristorante ristorante;

    /** Campo di testo per l'inserimento della recensione. */
    @FXML
    private TextArea txtTesto;

    /** Testo per indicare il nome del ristorante. */
    @FXML
    private Text txtNomeRistorante;

    /** Pulsante per inviare la recensione. */
    @FXML
    private Button btnInvia;

    /** RadioButton per selezionare 1 stella. */
    @FXML
    private RadioButton radioStella1;

    /** RadioButton per selezionare 2 stelle. */
    @FXML
    private RadioButton radioStella2;

    /** RadioButton per selezionare 3 stelle. */
    @FXML
    private RadioButton radioStella3;

    /** RadioButton per selezionare 4 stelle. */
    @FXML
    private RadioButton radioStella4;

    /** RadioButton per selezionare 5 stelle. */
    @FXML
    private RadioButton radioStella5;

    /** Gruppo di Toggle per gestire la selezione delle stelle. */
    @FXML
    private ToggleGroup stelleGroup;

    /**
     * Metodo di inizializzazione automatico chiamato da JavaFX dopo l'iniezione
     * degli elementi FXML. Aggiunge un listener al campo di testo per
     * controllare lo stato dei campi e abilitare/disabilitare il pulsante di invio.
     */
    @FXML
    private void initialize() {
        txtTesto.textProperty().addListener((_, _, _) -> controllaCampi());
    }

    /**
     * Imposta il ristorante a cui aggiungere la recensione.
     * Questo metodo viene tipicamente chiamato dal controller della pagina del ristorante.
     *
     * @param ristorante l'oggetto {@link Ristorante} di destinazione della recensione.
     */
    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;
        txtNomeRistorante.setText(ristorante.getNomeRistorante());
    }

    /**
     * Controlla se tutti i campi necessari per inviare la recensione
     * (testo e selezione delle stelle) sono stati compilati.
     * Abilita o disabilita il pulsante di invio di conseguenza.
     */
    private void controllaCampi() {
        boolean campiVuoti = txtTesto.getText().isBlank() ||
                stelleGroup.getSelectedToggle() == null;

        disabilitaBottone(btnInvia, campiVuoti);
    }

    /**
     * Gestisce l'invio della recensione. Recupera i dati inseriti dall'utente,
     * crea un nuovo oggetto {@link Recensione}, lo aggiunge al ristorante per il calcolo della media
     * e lo salva nel file CSV tramite {@link RecensioneCSV}.
     * Dopo il salvataggio, torna alla pagina del ristorante e chiude la finestra corrente.
     */
    @FXML
    private void aggiungiRecensione() {
        String testo = txtTesto.getText();
        RadioButton selectedStella = (RadioButton) stelleGroup.getSelectedToggle();
        int stelle = selectedStella.getText().length();

        Recensione recensione = new Recensione();
        recensione.setUsername(utenteLoggato.getUsername());
        recensione.setIdUtente(utenteLoggato.getId());
        recensione.setIdRistorante(ristorante.getId());
        recensione.setStelle(stelle);
        recensione.setTesto(testo);
        recensione.setRisposta("");
        recensione.setRistorante(ristorante);

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