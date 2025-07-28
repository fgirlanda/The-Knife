package com.gruppo10.controller;

import java.io.IOException;
import javafx.scene.control.Label;
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
import com.google.gson.JsonParser;
import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Criptatore;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;
import com.gruppo10.classi.UtenteReader;
import com.gruppo10.classi.UtenteWriter;

import javafx.fxml.FXML;
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

    private boolean paginaPrincipale;

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
    private Button btnRegistrati;
    
    @FXML
    private Label statusRegistrazione;
    
    private ToggleGroup ruoloGroup;
    

    public void initialize() {
        // Inizializza il ToggleGroup e associa i RadioButton
        ruoloGroup = new ToggleGroup();
        clienteRadioButton.setToggleGroup(ruoloGroup);
        ristoratoreRadioButton.setToggleGroup(ruoloGroup);

        // Aggiungi listener per abilitare/disabilitare il pulsante
        nomeTextField.textProperty().addListener((_, _, _) -> checkFields()); // (_, _, _) = (observable, oldValue, newValue)
        cognomeTextField.textProperty().addListener((_, _, _) -> checkFields());
        usernameTextField.textProperty().addListener((_, _, _) -> checkFields());
        passwordField.textProperty().addListener((_, _, _) -> checkFields());
        indirizzoTextField.textProperty().addListener((_, _, _) -> checkFields());
        dataNascitaPicker.valueProperty().addListener((_, _, _) -> checkFields());
        ruoloGroup.selectedToggleProperty().addListener((_, _, _) -> checkFields());

        // Autocompletamento con Nominatim
        TextFields.<String>bindAutoCompletion(indirizzoTextField, request -> {
            try {
                return getSuggestions(request.getUserText());
            } catch (Exception e) {
                return Collections.emptyList();
            }
        });
    }


    // Imposta il riferimento alla finestra principale (Stage)
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    

    public void setPrincipale(boolean paginaPrincipale){
        this.paginaPrincipale = paginaPrincipale;
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
        btnRegistrati.setDisable(!allFieldsFilled);
    }

    
    
    @FXML
    public void registrati() throws Exception {
        // Ottieni il ruolo selezionato
        RadioButton selectedRadioButton = (RadioButton) ruoloGroup.getSelectedToggle();
        String ruolo = selectedRadioButton.getText();
        
        // Ottieni i dati di registrazione (es. nome, cognome, username, password, ecc.) dai campi di input
        String nome = nomeTextField.getText();
        String cognome = cognomeTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        String indirizzo = indirizzoTextField.getText();
        
        // Verifica che l'username sia disponibile
        UtenteReader reader = new UtenteReader();
        if(reader.cercaUtente(username) != null) {
            // Mostra un messaggio di errore se l'username è già in uso
            statusRegistrazione.setText("Username già in uso. Scegli un altro username.");
            return;
        }
        
        // Formatta la data di nascita
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dataNascita = dataNascitaPicker.getValue().format(formatter);
        
        // Cripta la password
        try {
            password = Criptatore.cripta(password);
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
        Coordinate coordinate = new Coordinate(indirizzo);
        utente.setCords(coordinate);
        reader.aggiungiUtente(utente.getUsername(),utente);
        UtenteWriter writer = new UtenteWriter();
        try {
            writer.scriviUtente(utente);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Apri pagina di login
        apriLogin();
    }
    

    static List<String> getSuggestions(String query) throws IOException, InterruptedException {
        String url = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8)
        + "&format=json&addressdetails=1&limit=5";
        
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("User-Agent", "TuaApp/1.0 (tua@email.com)")
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
    private void annulla(){
        if(paginaPrincipale){
            SceneManager.cambioScena(stage, "/GUI/pagina_principale.fxml", "The Knife - Login", 
                (PaginaPrincipaleController controller) -> controller.setStage(stage));
        }else{
            apriLogin();
        }
    }


    private void apriLogin(){
        SceneManager.cambioScena(stage, "/GUI/login.fxml", "The Knife - Login", 
            (LoginController controller) -> controller.setStage(stage));
    }
}
