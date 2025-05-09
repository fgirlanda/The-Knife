// controller/LoginController.java
package com.gruppo10.controller;


import javafx.scene.control.Label;
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
            String username = usernameField.getText();
            String password = passwordField.getText();
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            
            //Prova per settare nuovo valore del login status
            loginStatus.setText("Login status: PASSWORD SBAGLIATA");
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
