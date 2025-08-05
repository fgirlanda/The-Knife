package com.gruppo10.controller;

import javafx.scene.control.Label;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Criptatore;
import com.gruppo10.classi.Indirizzi;
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

    @FXML private RadioButton clienteRadioButton;
    
    @FXML private RadioButton ristoratoreRadioButton;

    @FXML private TextField nomeTextField;
    
    @FXML private TextField cognomeTextField;
    
    @FXML private TextField usernameTextField;
    
    @FXML private PasswordField passwordField;
    
    @FXML private DatePicker dataNascitaPicker;
    
    @FXML private TextField indirizzoTextField;
    
    @FXML private Button btnRegistrati;
    
    @FXML private Label statusRegistrazione;
    
    @FXML private ToggleGroup ruoloGroup;
    

    public void initialize() {

        // Aggiungi listener per abilitare/disabilitare il pulsante
        nomeTextField.textProperty().addListener((_, _, _) -> controllaCampi()); // (_, _, _) = (observable, oldValue, newValue)
        cognomeTextField.textProperty().addListener((_, _, _) -> controllaCampi());
        usernameTextField.textProperty().addListener((_, _, _) -> controllaCampi());
        passwordField.textProperty().addListener((_, _, _) -> controllaCampi());
        indirizzoTextField.textProperty().addListener((_, _, _) -> controllaCampi());
        dataNascitaPicker.valueProperty().addListener((_, _, _) -> controllaCampi());
        ruoloGroup.selectedToggleProperty().addListener((_, _, _) -> controllaCampi());

        // Autocompletamento con Nominatim
        TextFields.<String>bindAutoCompletion(indirizzoTextField, request -> {
            try {
                return Indirizzi.getSuggestions(request.getUserText());
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

    
    private void controllaCampi() {
        boolean allFieldsFilled = !nomeTextField.getText().isEmpty() &&
                                  !cognomeTextField.getText().isEmpty() &&
                                  !usernameTextField.getText().isEmpty() &&
                                  !passwordField.getText().isEmpty() &&
                                  !indirizzoTextField.getText().isEmpty() &&
                                  dataNascitaPicker.getValue() != null &&
                                  ruoloGroup.getSelectedToggle() != null;

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
        if(UtenteReader.cercaUtente(username) != null) {
            statusRegistrazione.setText("Username già in uso. Scegli un altro username.");
            return;
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dataNascita = dataNascitaPicker.getValue().format(formatter);
        

        password = Criptatore.cripta(password);
 
        
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
        UtenteReader.aggiungiUtente(utente.getUsername(),utente);
        UtenteWriter writer = new UtenteWriter();
        try {
            writer.scriviUtente(utente);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Apri pagina di login
        apriLogin();
    }


    @FXML
    private void annulla(){
        if(paginaPrincipale){
            SceneManager.cambioScena(stage, "/GUI/pagina_principale.fxml", "The Knife - Login", // Gestire eccezioni cambio scena
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
