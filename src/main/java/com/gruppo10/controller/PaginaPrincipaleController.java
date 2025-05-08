package com.gruppo10.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PaginaPrincipaleController {

    private Stage stage;
    @FXML
    private Button bottoneRegistratiProfilo;

    // Imposta il riferimento alla finestra principale (Stage)
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void gestisciBottoneUtente(){
        String testo = bottoneRegistratiProfilo.getText().toLowerCase();
        System.out.println(testo);
        if (testo.equals("registrati")) {
            apriRegistrati();
        } else if (testo.equals("profilo")) {
            apriProfilo();
        }
    }

    @FXML
    private void apriRegistrati(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/registrazione.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Cambia scena nella stessa finestra (Stage)
            stage.setScene(scene);
            stage.setTitle("The Knife - Registrazione");
            RegistrazioneController controller = loader.getController();
            controller.setStage(stage);

            // Puoi aggiungere animazioni qui se vuoi (es. fade)

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void apriProfilo(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/profilo_cliente.fxml")); //gestire ruolo cliente o ristoratore
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Cambia scena nella stessa finestra (Stage)
            stage.setScene(scene);
            stage.setTitle("The Knife - Profilo");
            ProfiloClienteController controller = loader.getController();
            controller.setStage(stage);

            // Puoi aggiungere animazioni qui se vuoi (es. fade)

        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
}
