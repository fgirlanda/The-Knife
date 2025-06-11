package com.gruppo10.controller;

import java.io.IOException;

import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProfiloRistoratoreController {

    private Stage stage;

    private Utente utenteloggato = LoginController.utenteLoggato;

    // Imposta il riferimento alla finestra principale (Stage)
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void apri_aggiungi_ristorante() {
        try {
            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/aggiungi_ristorante.fxml"));
            Parent root = loader.load();

            // Crea un nuovo stage per il dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Aggiungi Ristorante");
            dialogStage.initModality(Modality.APPLICATION_MODAL); // Blocca l'interazione con altre finestre
            dialogStage.initOwner(stage);
            dialogStage.setScene(new Scene(root));

            AggiungiRistoranteController controller = loader.getController();
            controller.setStage(dialogStage);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}