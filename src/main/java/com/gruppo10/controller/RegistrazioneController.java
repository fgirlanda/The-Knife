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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Cryptatore;
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
import javafx.stage.Stage;

import org.controlsfx.control.textfield.TextFields;

public class RegistrazioneController {

    private Stage stage;

    // Imposta il riferimento alla finestra principale (Stage)
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private RadioButton clienteRadioButton;

    @FXML
    private RadioButton ristoratoreRadioButton;

    @FXML
    private TextField nomeTextField;

    @FXML
    private TextField cognomeTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private DatePicker dataNascitaPicker;

    @FXML
    private TextField indirizzoTextField;

    @FXML
    private Button registratiButton;

    private ToggleGroup ruoloGroup;

    @FXML
    public void initialize() {
        // Inizializza il ToggleGroup e associa i RadioButton
        ruoloGroup = new ToggleGroup();
        clienteRadioButton.setToggleGroup(ruoloGroup);
        ristoratoreRadioButton.setToggleGroup(ruoloGroup);

        // Aggiungi listener per abilitare/disabilitare il pulsante
        nomeTextField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        cognomeTextField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        usernameTextField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        indirizzoTextField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        dataNascitaPicker.valueProperty().addListener((observable, oldValue, newValue) -> checkFields());
        ruoloGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> checkFields());

        // Autocompletamento con Nominatim
        TextFields.<String>bindAutoCompletion(indirizzoTextField, request -> {
            try {
                return getSuggestions(request.getUserText());
            } catch (Exception e) {
                return Collections.emptyList();
            }
        });
    }

    private void checkFields() {
        // Controlla se tutti i campi sono riempiti
        boolean allFieldsFilled = !nomeTextField.getText().isEmpty() &&
                                  !cognomeTextField.getText().isEmpty() &&
                                  !usernameTextField.getText().isEmpty() &&
                                  !passwordField.getText().isEmpty() &&
                                  !indirizzoTextField.getText().isEmpty() &&
                                  dataNascitaPicker.getValue() != null &&
                                  ruoloGroup.getSelectedToggle() != null;

        // Abilita o disabilita il pulsante in base ai campi
        registratiButton.setDisable(!allFieldsFilled);
    }

    @FXML
    public void apriLogin(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Cambia scena nella stessa finestra (Stage)
            stage.setScene(scene);
            stage.setTitle("The Knife - Login");
            LoginController controller = loader.getController();
            controller.setStage(stage);

            // Puoi aggiungere animazioni qui se vuoi (es. fade)

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Altri metodi e logica del controller


    @FXML
    public void registrati() throws Exception {
        // Ottieni il ruolo selezionato
        RadioButton selectedRadioButton = (RadioButton) ruoloGroup.getSelectedToggle();
        String ruolo = selectedRadioButton.getText();

        // Ottieni i dati di registrazione (es. nome, cognome, username, password, ecc.) dai campi di input
        String nome = nomeTextField.getText().toUpperCase();
        String cognome = cognomeTextField.getText().toUpperCase();
        String username = usernameTextField.getText().toUpperCase();
        String password = passwordField.getText();
        String indirizzo = indirizzoTextField.getText().toUpperCase();

         // Formatta la data di nascita
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dataNascita = dataNascitaPicker.getValue().format(formatter);

        // Cripta la password
        Cryptatore cryptatore = new Cryptatore();
        try {
            password = cryptatore.crypta(password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crea un oggetto Utente e imposta i valori
        Utente utente = new Utente();
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setUsername(username);
        utente.setPassword(password);
        utente.setDataDiNascita(dataNascita);
        utente.setIndirizzo(indirizzo);
        utente.setRuolo(ruolo);

        // Ottieni coordinate
        Coordinate cords = geocode(indirizzo);
        // System.out.println(cords); // Debug
        utente.setCords(cords);

        UtenteWriter writer = new UtenteWriter();
        try {
            writer.scriviUtente(utente);
        } catch (Exception e) {
            e.printStackTrace();
        }


        
        // Apri pagina principale
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/pagina_principale.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Cambia scena nella stessa finestra (Stage)
            stage.setScene(scene);
            stage.setTitle("The Knife - Login");
            PaginaPrincipaleController controller = loader.getController();
            controller.setStage(stage);

            // Puoi aggiungere animazioni qui se vuoi (es. fade)

        } catch (Exception e) {
                e.printStackTrace();
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

    public Coordinate geocode(String address) throws Exception {
        String encodedAddress = address.replace(" ", "+");
        String url = "https://nominatim.openstreetmap.org/search?q=" + encodedAddress + "&format=json&limit=1";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("User-Agent", "JavaFXApp/1.0") // importante per Nominatim
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonArray results = JsonParser.parseString(response.body()).getAsJsonArray();
        if (results.size() == 0) return null;

        JsonObject obj = results.get(0).getAsJsonObject();
        double lat = obj.get("lat").getAsDouble();
        double lon = obj.get("lon").getAsDouble();

        Coordinate cords = new Coordinate();
        cords.setLat(lat);
        cords.setLon(lon);

        return cords;
    }
}
