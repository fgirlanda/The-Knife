// controller/LoginController.java
package com.gruppo10.controller;


import javafx.scene.control.Label;

import java.util.Collections;

import org.controlsfx.control.textfield.TextFields;

import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Criptatore;
import com.gruppo10.classi.Utente;
import com.gruppo10.classi.UtenteReader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    public static Utente utenteLoggato = null;

    private Stage stage;

    @FXML
    private TextField textIndirizzo;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label loginStatus;

    // Imposta il riferimento alla finestra principale
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void apriRegistrazione() {
        try {
            // Carica la nuova scena per la registrazione
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/registrazione.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            
            // Cambia scena nella stessa finestra
            stage.setScene(scene);
            stage.setTitle("The Knife - Registrazione");
            
            RegistrazioneController controller = loader.getController();
            controller.setStage(stage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodo per gestire il login
    @FXML
    public void provaLogin(ActionEvent event) {
        try {
            //Carico gli utenti registrati
            UtenteReader ur = new UtenteReader();

            String username = usernameField.getText();
            String password = passwordField.getText();
            String hashedPassword = Criptatore.cripta(password);
           
            // Verifica se l'utente esiste nel file CSV
            Utente utente = ur.cercaUtente(username);
            if (utente == null) {
                // Utente non trovato
                loginStatus.setText("Login status: UTENTE NON REGISTRATO");
                return;
            }
                
            if (hashedPassword.equals(utente.getPassword())) {
                // Login riuscito
                loginStatus.setText("Login status: LOGIN RIUSCITO");

                utenteLoggato = utente;

                // Carica la nuova scena per la pagina principale
            
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/pagina_principale.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                // Cambia scena nella stessa finestra (Stage)
                stage.setScene(scene);
                stage.setTitle("The Knife - Pagina Principale");
                PaginaPrincipaleController controller = loader.getController();
                controller.setStage(stage);
            

            } else {
                // Login fallito
                loginStatus.setText("Login status: PASSWORD ERRATA");
            }
  
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void initialize(){
             // Autocompletamento con Nominatim
        TextFields.<String>bindAutoCompletion(textIndirizzo, request -> {
            try {
                return RegistrazioneController.getSuggestions(request.getUserText());
            } catch (Exception e) {
                return Collections.emptyList();
            }
        });
    }


    @FXML
    public void continuaSenzaRegistrarti(ActionEvent event) {
        try {
            String indirizzo = textIndirizzo.getText();
            if (indirizzo.isEmpty()) {
                loginStatus.setText("Login status: INSERISCI UN INDIRIZZO");
                return;
            }
            utenteLoggato = new Utente();
            utenteLoggato.setRuolo("NON_REGISTRATO");
            utenteLoggato.setIndirizzo(indirizzo);
            Coordinate coordinate = new Coordinate(indirizzo);
            utenteLoggato.setCords(coordinate);
            // Carica la nuova scena per la pagina principale
        
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/pagina_principale.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Cambia scena nella stessa finestra (Stage)
            stage.setScene(scene);
            stage.setTitle("The Knife - Pagina Principale");
            PaginaPrincipaleController controller = loader.getController();
            controller.setStage(stage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
