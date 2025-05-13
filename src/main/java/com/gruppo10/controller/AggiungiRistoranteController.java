package com.gruppo10.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.controlsfx.control.textfield.TextFields;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Cryptatore;
import com.gruppo10.classi.TipoCucina;
import com.gruppo10.classi.Utente;
import com.gruppo10.classi.UtenteWriter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AggiungiRistoranteController {



    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

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
    private ComboBox<TipoCucina> comboCucina;

    @FXML
    private Button btnAnnulla;

    @FXML
    private Button btnAggiungiRistorante;

    private ToggleGroup deliveryGroup;
    private ToggleGroup prenotazioneGroup;

    @FXML
    private void initialize() {
        // Inizializza il ComboBox con i valori dell'enum TipoCucina
        comboCucina.getItems().setAll(TipoCucina.values());
        
        deliveryGroup = new ToggleGroup();
        prenotazioneGroup = new ToggleGroup();

        radioDeliverySi.setToggleGroup(deliveryGroup);
        radioDeliveryNo.setToggleGroup(deliveryGroup);
        radioPrenotazioneSi.setToggleGroup(prenotazioneGroup);
        radioPrenotazioneNo.setToggleGroup(prenotazioneGroup);
        comboCucina.setValue(TipoCucina.INTERNAZIONALE); // Imposta un valore di default

        //Aggiungi listener per abilitare/disabilitare il pulsante
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
    private void aggiungiRistorante() {
        // Recupera i dati dai campi
        RadioButton selectedDelivery = (RadioButton) deliveryGroup.getSelectedToggle();
        RadioButton selectedPrenotazione = (RadioButton) prenotazioneGroup.getSelectedToggle();
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

        // Logica per aggiungere il ristorante (esempio: stampa dei dati)
        System.out.println("Ristorante aggiunto:");
        System.out.println("Nome: " + nomeRistorante);
        System.out.println("Indirizzo: " + indirizzo);
        System.out.println("Delivery: " + delivery);
        System.out.println("Prenotazione Online: " + prenotazioneOnline);
        System.out.println("Tipo Cucina: " + tipoCucina);

        // Chiudi la finestra o esegui altre azioni
        if (stage != null) {
            stage.close();
        }
    }


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
    stage.close(); // chiude la finestra modale
}

}
