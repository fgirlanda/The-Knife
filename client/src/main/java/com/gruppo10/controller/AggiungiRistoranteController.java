/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10.client.controller;

import java.sql.SQLException;

import org.controlsfx.control.textfield.TextFields;

import com.gruppo10.common.classi.Coordinate;
import com.gruppo10.common.classi.Delivery;
import com.gruppo10.common.classi.GestioneEccezioni;
import com.gruppo10.common.classi.Indirizzi;
import com.gruppo10.common.classi.Prenotazione;
import com.gruppo10.common.classi.Prezzo;
import com.gruppo10.common.classi.Ristorante;
import com.gruppo10.common.classi.TipoCucina;
import com.gruppo10.common.classi.Utente;
import com.gruppo10.server.database.RistoranteDAO;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ComboBox;

/**
 * Controller per la vista che permette ai proprietari di aggiungere un nuovo ristorante.
 * Gestisce l'interazione con i campi del modulo per l'inserimento dei dati del ristorante,
 * la validazione degli input e il salvataggio dei dati nel database.
 */
public class AggiungiRistoranteController extends BasicController {

    /** L'utente attualmente loggato nell'applicazione. */
    private Utente utenteLoggato = LoginController.utenteLoggato;

    /** Callback da eseguire alla chiusura della finestra del controller. */
    private Runnable onCloseCallback;

    /** Il ristorante appena creato tramite il modulo. */
    private Ristorante nuovoRistorante = null;

    /** Campo di testo per inserire il nome del ristorante. */
    @FXML
    private TextField nomeRistoranteField;

    /** Campo di testo per inserire l'indirizzo del ristorante. */
    @FXML
    private TextField indirizzoField;

    /** RadioButton per selezionare "Delivery disponibile". */
    @FXML
    private RadioButton radioDeliverySi;

    /** RadioButton per selezionare "Delivery non disponibile". */
    @FXML
    private RadioButton radioDeliveryNo;

    /** RadioButton per selezionare "Prenotazione online disponibile". */
    @FXML
    private RadioButton radioPrenotazioneSi;

    /** RadioButton per selezionare "Prenotazione online non disponibile". */
    @FXML
    private RadioButton radioPrenotazioneNo;

    /** RadioButton per selezionare prezzo livello 1 (€). */
    @FXML
    private RadioButton radioPrezzo1;

    /** RadioButton per selezionare prezzo livello 2 (€€). */
    @FXML
    private RadioButton radioPrezzo2;

    /** RadioButton per selezionare prezzo livello 3 (€€€). */
    @FXML
    private RadioButton radioPrezzo3;

    /** RadioButton per selezionare prezzo livello 4 (€€€€). */
    @FXML
    private RadioButton radioPrezzo4;

    /** ComboBox per selezionare il tipo di cucina del ristorante. */
    @FXML
    private ComboBox<TipoCucina> comboCucina;

    /** Area di testo per inserire una descrizione del ristorante. */
    @FXML
    private TextArea txtDescrizione;

    /** Pulsante per confermare l'aggiunta del ristorante. */
    @FXML
    private Button btnAggiungiRistorante;

    /** Gruppo di RadioButton per la scelta del delivery (si/no). */
    @FXML
    private ToggleGroup deliveryGroup;

    /** Gruppo di RadioButton per la scelta della prenotazione online (si/no). */
    @FXML
    private ToggleGroup prenotazioneGroup;

    /** Gruppo di RadioButton per la scelta del prezzo (1-4). */
    @FXML
    private ToggleGroup prezzoGroup;

    /**
     * Metodo di inizializzazione chiamato automaticamente da JavaFX dopo l'iniezione degli elementi FXML.
     * Imposta i valori per i ComboBox e i RadioButton, configura l'autocompletamento per l'indirizzo
     * e aggiunge listener per la validazione dei campi.
     */
    public void initialize() {
        comboCucina.getItems().setAll(TipoCucina.values());
        comboCucina.setValue(TipoCucina.INTERNAZIONALE);

        radioPrezzo1.setUserData(Prezzo.€);
        radioPrezzo2.setUserData(Prezzo.€€);
        radioPrezzo3.setUserData(Prezzo.€€€);
        radioPrezzo4.setUserData(Prezzo.€€€€);

        radioDeliverySi.setUserData(Delivery.DELIVERY_DISPONIBILE);
        radioDeliveryNo.setUserData(Delivery.DELIVERY_NON_DISPONIBILE);

        radioPrenotazioneSi.setUserData(Prenotazione.PRENOTAZIONE_ONLINE_DISPONIBILE);
        radioPrenotazioneNo.setUserData(Prenotazione.PRENOTAZIONE_ONLINE_NON_DISPONIBILE);

        nomeRistoranteField.textProperty().addListener((_, _, _) -> controllaCampi());
        indirizzoField.textProperty().addListener((_, _, _) -> controllaCampi());
        comboCucina.valueProperty().addListener((_, _, _) -> controllaCampi());

        comboCucina.setVisibleRowCount(4);
        comboCucina.setMaxHeight(200);

        btnAggiungiRistorante.setDefaultButton(true);

        TextFields.<String>bindAutoCompletion(indirizzoField, request -> {
            return Indirizzi.getRisultati(request.getUserText());
        });
    }

    /**
     * Controlla se i campi necessari sono stati compilati e abilita o disabilita
     * il pulsante di aggiunta del ristorante di conseguenza.
     */
    private void controllaCampi() {
        boolean campiVuoti = nomeRistoranteField.getText().isEmpty() ||
                indirizzoField.getText().isEmpty() ||
                deliveryGroup.getSelectedToggle() == null ||
                prenotazioneGroup.getSelectedToggle() == null ||
                prezzoGroup.getSelectedToggle() == null ||
                comboCucina.getValue() == null;

        disabilitaBottone(btnAggiungiRistorante, campiVuoti);
    }

    /**
     * Gestisce l'aggiunta del ristorante.
     * Recupera i dati dai campi, crea un nuovo oggetto {@link Ristorante},
     * lo salva nel database e aggiorna la finestra chiamante solo dopo il successo.
     */
    @FXML
    private void aggiungiRistorante() {
        String indirizzo = indirizzoField.getText();
        Coordinate cords = new Coordinate(indirizzo);
        if (cords.getLat() == null)
            return;

        Delivery selectedDelivery = (Delivery) deliveryGroup.getSelectedToggle().getUserData();
        Prenotazione selectedPrenotazione = (Prenotazione) prenotazioneGroup.getSelectedToggle().getUserData();
        Prezzo selectedPrezzo = (Prezzo) prezzoGroup.getSelectedToggle().getUserData();

        String nomeRistorante = nomeRistoranteField.getText();
        
        int idProprietario = utenteLoggato.getId();
        Ristorante ristorante = new Ristorante();
        ristorante.setIdproprietario(idProprietario);
        ristorante.setNomeRistorante(nomeRistorante);
        ristorante.setIndirizzo(indirizzo);
        ristorante.setDelivery(selectedDelivery);
        ristorante.setPrenotazioneOnline(selectedPrenotazione);
        ristorante.setPrezzo(selectedPrezzo);
        ristorante.setTipoCucina(comboCucina.getValue());
        ristorante.setDescrizione(txtDescrizione.getText());

        ristorante.setCords(cords);

        try {
            this.nuovoRistorante = new RistoranteDAO().aggiungiRistorante(ristorante);
        } catch (IllegalArgumentException | SQLException e) {
            GestioneEccezioni.errore("Errore durante l'aggiunta del ristorante", e, false, null);
            return;
        }

        if (onCloseCallback != null) {
            onCloseCallback.run();
        }

        chiudi();
    }

    /**
     * Restituisce il nuovo ristorante creato dal controller.
     *
     * @return l'oggetto {@link com.gruppo10.common.classi.Ristorante} appena creato.
     */
    public Ristorante getNuovoRistorante() {
        return nuovoRistorante;
    }

    /**
     * Imposta una callback da eseguire alla chiusura della finestra del controller.
     * Notifica il controller chiamante che è stato aggiunto un nuovo ristorante, in modo da aggiornare direttamente il contenitore card.
     *
     * @param callback il {@link Runnable} da eseguire.
     */
    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }
}
