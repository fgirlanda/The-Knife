/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import org.controlsfx.control.textfield.TextFields;

import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Indirizzi;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.RistoranteCSV;
import com.gruppo10.classi.TipoCucina;
import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ComboBox;

public class AggiungiRistoranteController extends Controller{

    private Utente utenteLoggato = LoginController.utenteLoggato;

    private Runnable onCloseCallback;

    private Ristorante nuovoRistorante = null;

    @FXML
    private TextField txtNomeRistorante;

    @FXML
    private TextField txtIndirizzo;

    @FXML
    private RadioButton radioDeliverySi;

    @FXML
    private RadioButton radioDeliveryNo;

    @FXML
    private RadioButton radioPrenotazioneSi;

    @FXML
    private RadioButton radioPrenotazioneNo;

    @FXML
    private RadioButton radioPrezzo1;

    @FXML
    private RadioButton radioPrezzo2;

    @FXML
    private RadioButton radioPrezzo3;

    @FXML
    private RadioButton radioPrezzo4;

    @FXML
    private ComboBox<TipoCucina> comboCucina;

    @FXML
    private TextArea txtDescrizione;

    @FXML
    private Button btnAggiungiRistorante;

    // Radio buttons groups
    @FXML
    private ToggleGroup deliveryGroup;
    @FXML
    private ToggleGroup prenotazioneGroup;
    @FXML
    private ToggleGroup prezzoGroup;

    public void initialize() {
        // Inizializza il ComboBox con i valori dell'enum TipoCucina
        comboCucina.getItems().setAll(TipoCucina.values());
        comboCucina.setValue(TipoCucina.INTERNAZIONALE); // Imposta un valore di default

        // Aggiungi listener per abilitare/disabilitare il pulsante
        txtNomeRistorante.textProperty().addListener((_, _, _) -> controllaCampi());
        txtIndirizzo.textProperty().addListener((_, _, _) -> controllaCampi());
        comboCucina.valueProperty().addListener((_, _, _) -> controllaCampi());

        // Imposta una lunghezza massima per il popup della ComboBox e abilita lo scroll
        comboCucina.setVisibleRowCount(4); // Limita il numero di voci visibili nel dropdown
        comboCucina.setMaxHeight(200); // Imposta un'altezza massima per la lista

        // Autocompletamento con Nominatim
        TextFields.<String>bindAutoCompletion(txtIndirizzo, request -> {
            return Indirizzi.getRisultati(request.getUserText());
        });
    }

    private void controllaCampi() {
        boolean campiVuoti = txtNomeRistorante.getText().isEmpty() ||
                txtIndirizzo.getText().isEmpty() ||
                deliveryGroup.getSelectedToggle() == null ||
                prenotazioneGroup.getSelectedToggle() == null ||
                comboCucina.getValue() == null;

        disabilitaBottone(btnAggiungiRistorante, campiVuoti);
    }

    @FXML
    private void aggiungiRistorante() throws Exception {

        String indirizzo = txtIndirizzo.getText();
        Coordinate cords = new Coordinate(indirizzo);
        if (cords.getLat() == null)
            return;
        // Recupera i dati dai campi
        RadioButton selectedDelivery = (RadioButton) deliveryGroup.getSelectedToggle();
        RadioButton selectedPrenotazione = (RadioButton) prenotazioneGroup.getSelectedToggle();
        RadioButton selectedPrezzo = (RadioButton) prezzoGroup.getSelectedToggle();
        String tempDelivery = selectedDelivery.getText();
        String tempPrenotazione = selectedPrenotazione.getText();

        String nomeRistorante = txtNomeRistorante.getText();
        boolean delivery;
        boolean prenotazioneOnline;
        if (tempDelivery.equals("Sì")) {
            delivery = true;
        } else {
            delivery = false;
        }
        if (tempPrenotazione.equals("Sì")) {
            prenotazioneOnline = true;
        } else {
            prenotazioneOnline = false;
        }

        // Crea un oggetto Ristorante
        int idProprietario = utenteLoggato.getId();
        Ristorante ristorante = new Ristorante();
        ristorante.setIdproprietario(idProprietario);
        ristorante.setNomeRistorante(nomeRistorante);
        ristorante.setIndirizzo(indirizzo);
        ristorante.setDelivery(delivery);
        ristorante.setPrenotazioneOnline(prenotazioneOnline);
        ristorante.setPrezzo(selectedPrezzo.getText());
        ristorante.setTipoCucina(comboCucina.getValue());
        ristorante.setDescrizione(txtDescrizione.getText());

        this.nuovoRistorante = ristorante;

        ristorante.setCords(cords);

        try {
            RistoranteCSV ristoranteCSV = new RistoranteCSV();
            ristoranteCSV.scrivi(ristorante);
        } catch (Exception e) {
            return;
        }

        if (onCloseCallback != null) {
            onCloseCallback.run();
        }

        chiudi();
    }

    public Ristorante getNuovoRistorante() {
        return nuovoRistorante;
    }

    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }
}
