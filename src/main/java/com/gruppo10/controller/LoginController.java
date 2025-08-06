package com.gruppo10.controller;


import javafx.scene.control.Button;
import javafx.scene.control.Label;

import org.controlsfx.control.textfield.TextFields;

import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Criptatore;
import com.gruppo10.classi.Indirizzi;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;
import com.gruppo10.classi.UtenteReader;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    public static Utente utenteLoggato = null;

    private Stage stage;

    @FXML private TextField indirizzoField;

    @FXML private TextField usernameField;
    
    @FXML private TextField passwordField;

    @FXML private Button btnLogin;

    @FXML private Button btnContinua;
    
    @FXML private Label loginStatus;

    // Aggiungere controllaCampi
    public void initialize(){
        usernameField.textProperty().addListener((_, _, _) -> controllaCampi());
        passwordField.textProperty().addListener((_, _, _) -> controllaCampi());
        indirizzoField.textProperty().addListener((_, _, _) -> controllaCampi());

        // Autocompletamento con Nominatim
        TextFields.<String>bindAutoCompletion(indirizzoField, request -> {
                return Indirizzi.getRisultati(request.getUserText());
        });

        loginStatus.setVisible(false);
    }


    private void controllaCampi() {
        boolean userPassword = usernameField.getText().isBlank() || passwordField.getText().isBlank();
        boolean indirizzo = indirizzoField.getText().isBlank();

        btnLogin.setDisable(userPassword);
        btnContinua.setDisable(indirizzo);
    }


    // Imposta il riferimento alla finestra principale
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    
    @FXML
    public void provaLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String hashedPassword = Criptatore.cripta(password);

        if(hashedPassword == null) return;
        
        // Verifica se l'utente esiste nel file CSV
        Utente utente = UtenteReader.cercaUtente(username);
        if (utente == null) {
            loginStatus.setVisible(true);
            loginStatus.setText("Login status: UTENTE NON REGISTRATO");
            return;
        }
        
        if (hashedPassword.equals(utente.getPassword())) {
            // loginStatus.setText("Login status: LOGIN RIUSCITO"); // Eventuale intermezzo durante precaricamento pagina principale
            
            // Precaricamento pagina principale (?)
            utenteLoggato = utente;

            SceneManager.apriPaginaPrincipale(stage);       
        } else {
            loginStatus.setVisible(true);
            loginStatus.setText("Login status: PASSWORD ERRATA");
        }
    }
    
    
    @FXML
    private void apriRegistrazione() {
        SceneManager.apriRegistrati(stage, false);
    }


    @FXML
    public void continuaSenzaRegistrarti() {
        String indirizzo = indirizzoField.getText();
        Coordinate coordinate = new Coordinate(indirizzo);
        if(coordinate.getLat() == null) return;
        utenteLoggato = new Utente();
        utenteLoggato.setRuolo("NON_REGISTRATO");
        utenteLoggato.setIndirizzo(indirizzo);
        utenteLoggato.setCords(coordinate);

        // Precaricamento pagina principale (?)
    
        SceneManager.apriPaginaPrincipale(stage);
    }
}
