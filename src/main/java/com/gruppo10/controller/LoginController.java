// controller/LoginController.java
package com.gruppo10.controller;


import javafx.scene.control.Label;

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
    TextField usernameField;

    @FXML
    TextField passwordField;

    @FXML
    Label loginStatus;

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

            String username = usernameField.getText().toUpperCase();
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
                try {
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

            } else {
                // Login fallito
                loginStatus.setText("Login status: PASSWORD ERRATA");
            }
  
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @FXML
    public void continuaSenzaRegistrarti(ActionEvent event) {
        try {
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
