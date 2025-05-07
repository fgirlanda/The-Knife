package com.gruppo10.controller;

import java.time.format.DateTimeFormatter;

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
    public void registrati() {
        // Ottieni il ruolo selezionato
        RadioButton selectedRadioButton = (RadioButton) ruoloGroup.getSelectedToggle();
        String ruolo = selectedRadioButton.getText();

        // Ottieni i dati di registrazione (es. nome, cognome, username, password, ecc.) dai campi di input
        String nome = nomeTextField.getText();
        String cognome = cognomeTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        String indirizzo = indirizzoTextField.getText();

         // Formatta la data di nascita
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dataNascita = dataNascitaPicker.getValue().format(formatter);

        

        // Stampa il ruolo selezionato (per debug)
        System.out.println("Ruolo selezionato: " + ruolo + "\nNome: " + nome + "\nCognome: " + cognome + "\nUsername: " + username + "\nPassword: " + password + "\nData di Nascita: " + dataNascita + "\nIndirizzo: " + indirizzo);

        // // Apri la finestra di registrazione in base al ruolo selezionato
        // try {
        //     FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/registrazione" + ruolo + ".fxml"));
        //     Parent root = loader.load();
        //     Scene scene = new Scene(root);

        //     // Cambia scena nella stessa finestra (Stage)
        //     stage.setScene(scene);
        //     stage.setTitle("The Knife - Registrazione " + ruolo);
        //     RegistrazioneRistoratoreController controller = loader.getController();
        //     controller.setStage(stage);

        //     // Puoi aggiungere animazioni qui se vuoi (es. fade)

        // } catch (Exception e) {
        //     e.printStackTrace();
        }
}
