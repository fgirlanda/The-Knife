// controller/LoginController.java
package com.gruppo10.controller;


import javafx.scene.control.Label;

import com.gruppo10.classi.Criptatore;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    private Stage stage;

    @FXML
    TextField usernameField;

    @FXML
    TextField passwordField;

    @FXML
    Label loginStatus;

    // Imposta il riferimento alla finestra principale (Stage)
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
            
            
            // Cambia scena nella stessa finestra (Stage)
            stage.setScene(scene);
            stage.setTitle("The Knife - Registrazione");
            
            RegistrazioneController controller = loader.getController();
            controller.setStage(stage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodo per gestire il login
    // Questo metodo viene chiamato quando l'utente fa clic sul pulsante di login
    @FXML
    public void provaLogin(ActionEvent event) {
        try {
            //prova controllo login
            String nome = "CICCIA";
            String passw = "121299f724378a9f0e487e9c124665c53cbbdf12f9c3e144e53808a2dc371f16";
            
            String username = usernameField.getText();
            String password = passwordField.getText();
            Criptatore criptatore = new Criptatore();
            String hashedPassword = criptatore.cripta(password);
            System.out.println("Username: " + username);
            System.out.println("Password: " + hashedPassword);
            
            if (username.toUpperCase().equals(nome) && hashedPassword.equals(passw)) {
                // Login riuscito
                System.out.println("Login riuscito");
                //Prova per settare nuovo valore del login status
                loginStatus.setText("Login status: LOGIN RIUSCITO");
            } else {
                // Login fallito
                System.out.println("Login fallito");
                //Prova per settare nuovo valore del login status
                loginStatus.setText("Login status: LOGIN FALLITO");
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
