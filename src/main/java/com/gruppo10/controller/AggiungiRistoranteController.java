package com.gruppo10.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.controlsfx.control.textfield.TextFields;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.RistoranteWriter;
import com.gruppo10.classi.TipoCucina;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import javafx.scene.control.ComboBox;

public class AggiungiRistoranteController {

    private Stage stage;
    
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
    private Button btnAnnulla;
    
    @FXML
    private Button btnAggiungiRistorante;
    
    // Radio buttons groups
    private ToggleGroup deliveryGroup;
    private ToggleGroup prenotazioneGroup;
    private ToggleGroup prezzoGroup;
    

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {

        // Inizializza il ComboBox con i valori dell'enum TipoCucina
        comboCucina.getItems().setAll(TipoCucina.values());
        comboCucina.setValue(TipoCucina.INTERNAZIONALE); // Imposta un valore di default
        
        deliveryGroup = new ToggleGroup();
        prenotazioneGroup = new ToggleGroup();
        prezzoGroup = new ToggleGroup();

        radioDeliverySi.setToggleGroup(deliveryGroup);
        radioDeliveryNo.setToggleGroup(deliveryGroup);
        radioPrenotazioneSi.setToggleGroup(prenotazioneGroup);
        radioPrenotazioneNo.setToggleGroup(prenotazioneGroup);
        radioPrezzo1.setToggleGroup(prezzoGroup);
        radioPrezzo2.setToggleGroup(prezzoGroup);
        radioPrezzo3.setToggleGroup(prezzoGroup);
        radioPrezzo4.setToggleGroup(prezzoGroup);

        // Aggiungi listener per abilitare/disabilitare il pulsante
        txtNomeRistorante.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        txtIndirizzo.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        comboCucina.valueProperty().addListener((observable, oldValue, newValue) -> checkFields());

        // Imposta una lunghezza massima per il popup della ComboBox e abilita lo scroll
        comboCucina.setVisibleRowCount(4); // Limita il numero di voci visibili nel dropdown
        comboCucina.setMaxHeight(200); // Imposta un'altezza massima per la lista

        
        // Autocompletamento con Nominatim
        TextFields.<String>bindAutoCompletion(txtIndirizzo, request -> {
            try {
                return getSuggestions(request.getUserText());
            } catch (Exception e) {
                return Collections.emptyList();
            }
        });

        checkFields();
   
    }

    private void checkFields() {

        // Controlla se tutti i campi sono riempiti
        boolean allFieldsFilled = !txtNomeRistorante.getText().isEmpty() &&
                                  !txtIndirizzo.getText().isEmpty() &&
                                  deliveryGroup.getSelectedToggle() != null &&
                                  prenotazioneGroup.getSelectedToggle() != null &&
                                    comboCucina.getValue() != null;
                                  
        // Abilita o disabilita il pulsante in base ai campi
        btnAggiungiRistorante.setDisable(!allFieldsFilled);
    }

    @FXML
    private void aggiungiRistorante() throws Exception {

        // Recupera i dati dai campi
        RadioButton selectedDelivery = (RadioButton) deliveryGroup.getSelectedToggle();
        RadioButton selectedPrenotazione = (RadioButton) prenotazioneGroup.getSelectedToggle();
        RadioButton selectedPrezzo = (RadioButton) prezzoGroup.getSelectedToggle();
        String tempDelivery = selectedDelivery.getText();
        String tempPrenotazione = selectedPrenotazione.getText();

        String nomeRistorante = txtNomeRistorante.getText();
        String indirizzo = txtIndirizzo.getText();
        boolean delivery;
        boolean prenotazioneOnline;
        if(tempDelivery.equals("Sì")) {delivery = true;} else {delivery = false;}
        if (tempPrenotazione.equals("Sì")) {prenotazioneOnline = true;} else {prenotazioneOnline = false;}
    
        String tipoCucina = comboCucina.getValue().toString();

        // Verifica che tutti i campi obbligatori siano compilati
        if (nomeRistorante.isEmpty() || indirizzo.isEmpty() || tipoCucina == null) {
            System.out.println("Compila tutti i campi obbligatori!");
            return;
        }

        // Crea un oggetto Ristorante
        Ristorante ristorante = new Ristorante();
        ristorante.setNomeRistorante(nomeRistorante);
        ristorante.setIndirizzo(indirizzo);
        ristorante.setDelivery(delivery);
        ristorante.setPrenotazioneOnline(prenotazioneOnline);
        ristorante.setPrezzo(selectedPrezzo.getText());
        ristorante.setTipoCucina(comboCucina.getValue());
        ristorante.setDescrizione(txtDescrizione.getText());

        Coordinate cords = new Coordinate(indirizzo);
        ristorante.setCords(cords);

        RistoranteWriter writer = new RistoranteWriter();
        try {
            writer.scriviRistorante(ristorante);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Chiudi la finestra o esegui altre azioni
        if (stage != null) {
            stage.close();
        }
    }


    // Autocompletamento con Nominatim
    private List<String> getSuggestions(String query) throws IOException, InterruptedException {
        String url = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8)
                     + "&format=json&addressdetails=1&limit=5";

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("User-Agent", "TheKnife/1.0 (fgirlanda@studenti.uninsubria.it)")
            .GET()
            .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString());

        JsonArray results = JsonParser.parseString(response.body()).getAsJsonArray();
        List<String> suggestions = new ArrayList<>();

        for (JsonElement result : results) {
            suggestions.add(result.getAsJsonObject().get("display_name").getAsString());
        }
        return suggestions;
    }

    
    @FXML
    private void annulla() {
        stage.close(); // Chiude la finestra
    }
}
