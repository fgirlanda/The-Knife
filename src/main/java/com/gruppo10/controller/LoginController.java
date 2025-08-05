package com.gruppo10.controller;


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

    @FXML private TextField textIndirizzo;

    @FXML private TextField usernameField;
    
    @FXML private TextField passwordField;
    
    @FXML private Label loginStatus;

    // Aggiungere controllaCampi

    public void initialize(){
        // Autocompletamento con Nominatim
        TextFields.<String>bindAutoCompletion(textIndirizzo, request -> {
                return Indirizzi.getRisultati(request.getUserText());
        });

        loginStatus.setVisible(false);
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
        String indirizzo = textIndirizzo.getText();

        if (indirizzo.isBlank()) {
            loginStatus.setVisible(true);
            loginStatus.setText("Login status: INSERISCI UN INDIRIZZO");
            return;
        }

        utenteLoggato = new Utente();
        utenteLoggato.setRuolo("NON_REGISTRATO");
        utenteLoggato.setIndirizzo(indirizzo);
        Coordinate coordinate = new Coordinate(indirizzo);
        utenteLoggato.setCords(coordinate);

        // Precaricamento pagina principale (?)
    
        SceneManager.apriPaginaPrincipale(stage);
    }
}
